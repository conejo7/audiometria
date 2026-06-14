package com.audiometria.audiometria.api.service.reporte;

import com.audiometria.audiometria.api.pagination.*;
import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.musicoteca.MusicotecaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public ExportExcelService(MusicaSystemService musicaSystemService, MusicotecaRepository musicotecaRepository) {
        this.musicaSystemService = musicaSystemService;
        this.musicotecaRepository = musicotecaRepository;
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

}
