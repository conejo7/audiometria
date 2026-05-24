package com.audiometria.audiometria.api.controllers.Batch;

import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class CsvMusicotecaReader extends FlatFileItemReader<Musicoteca> {

    // Columnas base (32) — formato original
    private static final String[] BASE_COLUMNS = {
            "saleStartDate", "saleEndDate", "dsp", "saleStoreName", "saleType", "saleUserType",
            "territory", "productUpc", "productReference", "productCatalogNumber", "productLabel",
            "productArtist", "productTitle", "assetArtist", "assetTitle", "assetVersion", "assetDuration",
            "assetIsrc", "assetReference", "assetProduct", "productQuantity", "assetQuantity",
            "originalGrossIncome", "originalCurrency", "exchangeRate", "convertedGrossIncome",
            "contractDealTerm", "reportedRoyalty", "currency", "reportRunId", "reportId", "saleId"
    };

    // Columnas base + audioFormat (33)
    private static final String[] COLUMNS_33 = {
            "saleStartDate", "saleEndDate", "dsp", "saleStoreName", "saleType", "saleUserType",
            "territory", "productUpc", "productReference", "productCatalogNumber", "productLabel",
            "productArtist", "productTitle", "assetArtist", "assetTitle", "assetVersion", "assetDuration",
            "assetIsrc", "assetReference", "assetProduct", "productQuantity", "assetQuantity",
            "originalGrossIncome", "originalCurrency", "exchangeRate", "convertedGrossIncome",
            "contractDealTerm", "reportedRoyalty", "currency", "reportRunId", "reportId", "saleId",
            "audioFormat"
    };

    // Columnas base + audioFormat + dspUnitId + dspContainerId + dspCollectionId (36)
    private static final String[] COLUMNS_36 = {
            "saleStartDate", "saleEndDate", "dsp", "saleStoreName", "saleType", "saleUserType",
            "territory", "productUpc", "productReference", "productCatalogNumber", "productLabel",
            "productArtist", "productTitle", "assetArtist", "assetTitle", "assetVersion", "assetDuration",
            "assetIsrc", "assetReference", "assetProduct", "productQuantity", "assetQuantity",
            "originalGrossIncome", "originalCurrency", "exchangeRate", "convertedGrossIncome",
            "contractDealTerm", "reportedRoyalty", "currency", "reportRunId", "reportId", "saleId",
            "audioFormat", "dspUnitId", "dspContainerId", "dspCollectionId"
    };

    public CsvMusicotecaReader(String filePath, UUID uploadId, String fecha, String euro) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath no puede ser null o vacío");
        }

        int columnCount = validateAndCountColumns(filePath);

        setResource(new FileSystemResource(filePath));
        setLinesToSkip(1);
        setEncoding("UTF-8");

        DefaultLineMapper<Musicoteca> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        switch (columnCount) {
            case 32 -> tokenizer.setNames(BASE_COLUMNS);
            case 33 -> tokenizer.setNames(COLUMNS_33);
            case 36 -> tokenizer.setNames(COLUMNS_36);
            default -> throw new IllegalArgumentException(
                    String.format("El archivo CSV tiene %d columnas. Se esperaban 32, 33 o 36 columnas.", columnCount)
            );
        }

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new MusicotecaFieldSetMapper(uploadId, fecha, euro));
        setLineMapper(lineMapper);
    }

    private int validateAndCountColumns(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();

            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new IllegalArgumentException("El archivo CSV está vacío o no tiene encabezado");
            }

            String[] columns = headerLine.split(",", -1);
            int columnCount = columns.length;

            if (columnCount != 32 && columnCount != 33 && columnCount != 36) {
                throw new IllegalArgumentException(
                        String.format("El archivo CSV tiene %d columnas. Se esperaban 32, 33 o 36 columnas.", columnCount)
                );
            }

            return columnCount;

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo CSV para validación: " + e.getMessage(), e);
        }
    }
}
