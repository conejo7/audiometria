package com.audiometria.audiometria.api.service.reporte;

import com.audiometria.audiometria.api.pagination.*;
import com.audiometria.audiometria.api.repository.dto.estadisticas.MusicotecaRoyaltyDetailDTO;
import com.audiometria.audiometria.api.repository.entities.estadisticas.MusicotecaRoyaltyDetailRepository;
import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.musicoteca.MusicotecaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExportExcelService {

    private final MusicaSystemService musicaSystemService;

    private final MusicotecaRepository musicotecaRepository;

    private final MusicotecaRoyaltyDetailRepository musicotecaRoyaltyDetailRepository;

    public ExportExcelService(MusicaSystemService musicaSystemService, MusicotecaRepository musicotecaRepository, MusicotecaRoyaltyDetailRepository musicotecaRoyaltyDetailRepository) {
        this.musicaSystemService = musicaSystemService;
        this.musicotecaRepository = musicotecaRepository;
        this.musicotecaRoyaltyDetailRepository = musicotecaRoyaltyDetailRepository;
    }

    public byte[] generarExcel(SearchRequest request) throws IOException {

        validarRangoFechas(request);

        List<Musicoteca> registros =
                searchMusicaSystemWithoutPagination(request);

        log.info("Registros encontrados: {}", registros.size());
        if (registros.size() > 500000) {
            throw new IllegalArgumentException(
                    "La exportación supera el límite permitido de registros");
        }

//        Workbook workbook = new XSSFWorkbook();
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        workbook.setCompressTempFiles(true);
        Sheet sheet = workbook.createSheet("Royalties");

        int rowNum = 0;

        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("PROVEEDOR");
        header.createCell(1).setCellValue("TIPO VENTA");
        header.createCell(2).setCellValue("TERRITORIO");
        header.createCell(3).setCellValue("SELLO");
        header.createCell(4).setCellValue("ALBUM");
        header.createCell(5).setCellValue("ARTISTA");
        header.createCell(6).setCellValue("GANANCIA");

        for (Musicoteca item : registros) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(item.getSaleStoreName());
            row.createCell(1).setCellValue(item.getSaleType());
            row.createCell(2).setCellValue(item.getTerritory());
            row.createCell(3).setCellValue(item.getProductLabel());
            row.createCell(4).setCellValue(item.getProductTitle());
            row.createCell(5).setCellValue(item.getAssetArtist());
            row.createCell(6).setCellValue(
                    item.getReportedRoyalty() != null
                            ? item.getReportedRoyalty().doubleValue()
                            : 0
            );
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    public List<Musicoteca> searchMusicaSystemWithoutPagination(SearchRequest request) {
        log.info("Searching musicoteca without pagination with request: {}", request);

        // Filtra cualquier intento de sobreescribir el user_id
        List<FilterRequest> cleanedFilters = request.getFilters().stream()
                .filter(f -> !"user_id".equalsIgnoreCase(f.getKey()))
                .collect(Collectors.toList());

        request.setFilters(cleanedFilters);

        String productLabel = Optional.ofNullable(request.getProductLabel())
                .filter(s -> !s.isBlank())
                .orElseGet(() -> request.getFilters().stream()
                        .filter(f -> "productLabel".equalsIgnoreCase(f.getKey()))
                        .map(FilterRequest::getValue)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .findFirst()
                        .orElse(null));

        if ("admin".equalsIgnoreCase(productLabel)) {
            log.info("Usuario admin detectado. Eliminando filtro productLabel.");
            request.setFilters(
                    request.getFilters().stream()
                            .filter(f -> !"productLabel".equalsIgnoreCase(f.getKey()))
                            .collect(Collectors.toList())
            );
        } else if (productLabel != null && !productLabel.isBlank()) {
            request.getFilters().add(new FilterRequest(
                    "productLabel",
                    Operator.EQUAL,
                    FieldType.STRING,
                    productLabel,
                    null,
                    null
            ));
        }

        request.getFilters().add(
                new FilterRequest(
                        "state",
                        Operator.EQUAL,
                        FieldType.LONG,
                        1,
                        null,
                        null
                )
        );

        SearchSpecification<Musicoteca> specification =
                new SearchSpecification<>(request);

        return musicotecaRepository.findAll(specification);
    }

    private void validarRangoFechas(SearchRequest request) {

        request.getFilters().stream()
                .filter(filter ->
                        filter.getOperator() == Operator.BETWEEN
                                && filter.getFieldType() == FieldType.DATE)
                .findFirst()
                .ifPresent(filter -> {

                    LocalDateTime fechaInicio =
                            (LocalDateTime) FieldType.DATE.parse(
                                    filter.getValue().toString());

                    LocalDateTime fechaFin =
                            (LocalDateTime) FieldType.DATE.parse(
                                    filter.getValueTo().toString());

                    if (fechaInicio.plusMonths(3).isBefore(fechaFin)) {
                        throw new IllegalArgumentException(
                                "Solo se permite exportar información de hasta 3 meses");
                    }
                });
    }


    /*
     * NUEVO:
     * Genera Excel con los resultados agrupados.
     */
    public byte[] generarExcelGrouped(
            SearchRequest request,
            String groupBy
    ) throws IOException {

        /*
         * Obtenemos TODOS los resultados agrupados,
         * sin paginación.
         */
        List<MusicotecaRoyaltyDetailDTO> datos =
                musicotecaRoyaltyDetailRepository
                        .searchGroupedForExport(
                                request,
                                groupBy
                        );


        try (
                SXSSFWorkbook workbook =
                        new SXSSFWorkbook(100);

                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            /*
             * Comprime los archivos temporales.
             */
            workbook.setCompressTempFiles(true);


            /*
             * Creamos la hoja.
             */
            Sheet sheet =
                    workbook.createSheet(
                            getSheetName(groupBy)
                    );


            /*
             * ==============================
             * ESTILOS
             * ==============================
             */

            // Cabecera
            CellStyle headerStyle =
                    workbook.createCellStyle();

            Font headerFont =
                    workbook.createFont();

            headerFont.setBold(true);

            headerStyle.setFont(headerFont);


            // Número entero para Assets
            CellStyle integerStyle =
                    workbook.createCellStyle();

            integerStyle.setDataFormat(
                    workbook
                            .createDataFormat()
                            .getFormat("#,##0")
            );


            // Moneda para Royalty
            CellStyle moneyStyle =
                    workbook.createCellStyle();

            DataFormat dataFormat =
                    workbook.createDataFormat();

            moneyStyle.setDataFormat(
                    dataFormat.getFormat("$#,##0.00")
            );


            /*
             * ==============================
             * CABECERA
             * ==============================
             */

            Row headerRow =
                    sheet.createRow(0);

            String[] headers = {
                    getGroupColumnName(groupBy),
                    "Assets",
                    "Report Royalty"
            };


            for (int i = 0; i < headers.length; i++) {

                Cell cell =
                        headerRow.createCell(i);

                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }


            /*
             * ==============================
             * DATOS
             * ==============================
             */

            int rowIndex = 1;

            for (MusicotecaRoyaltyDetailDTO dato : datos) {

                Row row =
                        sheet.createRow(rowIndex++);


                /*
                 * Columna agrupada.
                 */
                Cell groupCell =
                        row.createCell(0);

                groupCell.setCellValue(
                        getGroupValue(
                                dato,
                                groupBy
                        )
                );


                /*
                 * Assets.
                 */
                Cell assetCell =
                        row.createCell(1);

                assetCell.setCellValue(
                        dato.getAssetQuantity() != null
                                ? dato.getAssetQuantity().doubleValue()
                                : 0
                );

                assetCell.setCellStyle(integerStyle);


                /*
                 * Report Royalty.
                 */
                Cell royaltyCell =
                        row.createCell(2);

                BigDecimal totalRoyalty =
                        dato.getTotalRoyalty();

                royaltyCell.setCellValue(
                        totalRoyalty != null
                                ? totalRoyalty.doubleValue()
                                : 0
                );

                royaltyCell.setCellStyle(moneyStyle);
            }


            /*
             * ==============================
             * ANCHO DE COLUMNAS
             * ==============================
             */

            sheet.setColumnWidth(
                    0,
                    45 * 256
            );

            sheet.setColumnWidth(
                    1,
                    18 * 256
            );

            sheet.setColumnWidth(
                    2,
                    20 * 256
            );


            /*
             * Escribimos el archivo.
             */
            workbook.write(outputStream);


            /*
             * Retornamos byte[] para tu controller.
             */
            return outputStream.toByteArray();
        }
    }

        /*
         * Nombre de la hoja.
         */
        private String getSheetName(
                String groupBy
    ) {

            return switch (groupBy) {

                case "dsp" ->
                        "DSP";

                case "territory" ->
                        "Territory";

                case "productTitle" ->
                        "Product Title";

                case "assetTitle" ->
                        "Asset Title";

                default ->
                        "Estadisticas";
            };
        }


        /*
         * Nombre de la primera columna.
         */
        private String getGroupColumnName(
                String groupBy
    ) {

            return switch (groupBy) {

                case "dsp" ->
                        "DSP";

                case "territory" ->
                        "Territory";

                case "productTitle" ->
                        "Product Title";

                case "assetTitle" ->
                        "Asset Title";

                default ->
                        "Grupo";
            };
        }


        /*
         * Obtiene el valor correspondiente
         * según el tipo de agrupación.
         */
        private String getGroupValue(
                MusicotecaRoyaltyDetailDTO dto,
                String groupBy
    ) {

            return switch (groupBy) {

                case "dsp" ->
                        dto.getDsp() != null
                                ? dto.getDsp()
                                : "";

                case "territory" ->
                        dto.getTerritory() != null
                                ? dto.getTerritory()
                                : "";

                case "productTitle" ->
                        dto.getProductTitle() != null
                                ? dto.getProductTitle()
                                : "";

                case "assetTitle" ->
                        dto.getAssetTitle() != null
                                ? dto.getAssetTitle()
                                : "";

                default ->
                        "";
            };
        }

}
