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

    public CsvMusicotecaReader(String filePath, UUID uploadId, String fecha, String euro) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath no puede ser null o vacío");
        }

        int columnCount = validateAndCountColumns(filePath);

        setResource(new FileSystemResource(filePath));
        setLinesToSkip(1); // skip header
        setEncoding("UTF-8");

        DefaultLineMapper<Musicoteca> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        // Configurar nombres de columnas según el número detectado
        if (columnCount == 32) {
            // Archivo sin "Audio Format"
            tokenizer.setNames(
                    "saleStartDate", "saleEndDate", "dsp", "saleStoreName", "saleType", "saleUserType",
                    "territory", "productUpc", "productReference", "productCatalogNumber", "productLabel",
                    "productArtist", "productTitle", "assetArtist", "assetTitle", "assetVersion", "assetDuration",
                    "assetIsrc", "assetReference", "assetProduct", "productQuantity", "assetQuantity",
                    "originalGrossIncome", "originalCurrency", "exchangeRate", "convertedGrossIncome",
                    "contractDealTerm", "reportedRoyalty", "currency", "reportRunId", "reportId", "saleId"
            );
        } else if (columnCount == 33) {
            // Archivo con "Audio Format"
            tokenizer.setNames(
                    "saleStartDate", "saleEndDate", "dsp", "saleStoreName", "saleType", "saleUserType",
                    "territory", "productUpc", "productReference", "productCatalogNumber", "productLabel",
                    "productArtist", "productTitle", "assetArtist", "assetTitle", "assetVersion", "assetDuration",
                    "assetIsrc", "assetReference", "assetProduct", "productQuantity", "assetQuantity",
                    "originalGrossIncome", "originalCurrency", "exchangeRate", "convertedGrossIncome",
                    "contractDealTerm", "reportedRoyalty", "currency", "reportRunId", "reportId", "saleId",
                    "audioFormat"
            );
        } else {
            throw new IllegalArgumentException(
                    String.format("El archivo CSV tiene %d columnas. Se esperaban 32 o 33 columnas.", columnCount)
            );
        }

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new MusicotecaFieldSetMapper(uploadId, fecha, euro));
        setLineMapper(lineMapper);

//        lineMapper.setLineTokenizer(new DelimitedLineTokenizer() {{
//            setNames(
//                    "saleStartDate", "saleEndDate", "dsp", "saleStoreName", "saleType", "saleUserType",
//                    "territory", "productUpc", "productReference", "productCatalogNumber", "productLabel",
//                    "productArtist", "productTitle", "assetArtist", "assetTitle", "assetVersion", "assetDuration",
//                    "assetIsrc", "assetReference", "assetProduct", "productQuantity", "assetQuantity",
//                    "originalGrossIncome", "originalCurrency", "exchangeRate", "convertedGrossIncome",
//                    "contractDealTerm", "reportedRoyalty", "currency", "reportRunId", "reportId", "saleId"
//            );
//        }});
//        lineMapper.setFieldSetMapper(new MusicotecaFieldSetMapper(uploadId,fecha, euro));
//        setLineMapper(lineMapper);
    }

    /**
     * Valida y cuenta el número de columnas en el archivo CSV
     * @param filePath ruta del archivo
     * @return número de columnas encontradas
     */
    private int validateAndCountColumns(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();

            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new IllegalArgumentException("El archivo CSV está vacío o no tiene encabezado");
            }

            // Contar columnas separadas por coma
            String[] columns = headerLine.split(",", -1);
            int columnCount = columns.length;

            // Validar que sea 32 o 33
            if (columnCount != 32 && columnCount != 33) {
                throw new IllegalArgumentException(
                        String.format("El archivo CSV tiene %d columnas. Se esperaban 32 o 33 columnas.", columnCount)
                );
            }

            return columnCount;

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo CSV para validación: " + e.getMessage(), e);
        }
    }
}
