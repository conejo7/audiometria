package com.audiometria.audiometria.api.service.reporte;


import com.audiometria.audiometria.api.pagination.*;
import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.musicoteca.MusicotecaRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MusicaSystemService {

    private final MusicotecaRepository musicotecaRepository;



    public MusicaSystemService(MusicotecaRepository musicotecaRepository) {
        this.musicotecaRepository = musicotecaRepository;

    }

    public Page<Musicoteca> searchMusicaSystem(SearchRequest request) {
        log.info("Searching musicoteca with request: {}", request);
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

        if (productLabel != null && !productLabel.isBlank()) {
            request.getFilters().add(new FilterRequest(
                    "productLabel",
                    Operator.EQUAL,
                    FieldType.STRING,
                    productLabel,
                    null, null
            ));
        } else {
            log.warn("product_label no enviado; se omitirá ese filtro.");
        }
        request.getFilters().add(
                new FilterRequest(
                        "state",
                        Operator.EQUAL,
                        FieldType.LONG,   // O BOOLEAN si tu campo es boolean
                        1,
                        null,
                        null
                )
        );

        SearchSpecification<Musicoteca> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return musicotecaRepository.findAll(specification, pageable);
    }


    public void cargarCsv(MultipartFile file, UUID userId) {

        try (InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(inputStreamReader)) {

            String[] data;
            boolean isFirstRow = true;

            // Read CSV line by line
            while ((data = csvReader.readNext()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip header
                }

                if (data.length < 32) {
                    log.info("Fila incompleta: " + Arrays.toString(data));
                    continue;
                }
                Musicoteca m = new Musicoteca();
                m.setProductLabel(data[10]);//el product label es el usuario
                m.setSaleStartDate(parseDate(data[0]));
                m.setSaleEndDate(parseDate(data[1]));
                m.setDsp(data[2]);
                m.setSaleStoreName(data[3]);
                m.setSaleType(data[4]);
                m.setSaleUserType(data[5]);
                m.setTerritory(data[6]);
                m.setProductUpc(data[7]);
                m.setProductReference(data[8]);
                m.setProductCatalogNumber(data[9]);

                m.setProductArtist(data[11]);
                m.setProductTitle(data[12]);
                m.setAssetArtist(data[13]);
                m.setAssetTitle(data[14]);
                m.setAssetVersion(data[15]);
                m.setAssetDuration(data[16]);
                m.setAssetIsrc(data[17]);
                m.setAssetReference(data[18]);
                m.setAssetProduct(data[19]);
                m.setProductQuantity(parseInteger(data[20]));
                m.setAssetQuantity(parseInteger(data[21]));
                m.setOriginalGrossIncome(parseBigDecimal(data[22]));
                m.setOriginalCurrency(data[23]);
                m.setExchangeRate(parseBigDecimal(data[24]));
                m.setConvertedGrossIncome(parseBigDecimal(data[25]));
                m.setContractDealTerm(data[26]);
                m.setReportedRoyalty(parseBigDecimal(data[27]));
                m.setCurrency(data[28]);
                m.setReportRunId(data[29]);
                m.setReportId(data[30]);
                m.setSaleId(data[31]);
                m.setCreatedAt(LocalDateTime.now());
                m.setDateUser(LocalDateTime.now());
                m.setState(1L);

                musicotecaRepository.save(m);
                log.info("Registro guardado: " + m);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException("Error al procesar el archivo CSV: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage(), e);
        }
    }


    private Integer parseInteger(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Integer.parseInt(value.trim()) : null;
        } catch (NumberFormatException e) {
//            log.warning("Error parsing integer: " + value);
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? new BigDecimal(value.trim()) : null;
        } catch (NumberFormatException e) {
//            log.warning("Error parsing BigDecimal: " + value);
            return null;
        }
    }


    private String getString(Cell cell) {
        if (cell == null) return null;
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private Integer getInteger(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return (int) cell.getNumericCellValue();
    }

    private BigDecimal getBigDecimal(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return BigDecimal.valueOf(cell.getNumericCellValue());
    }

    private LocalDateTime getDate(Cell cell) {
        if (cell == null || !DateUtil.isCellDateFormatted(cell)) return null;
        return cell.getLocalDateTimeCellValue();
    }

    private LocalDateTime parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
    }



}
