package com.audiometria.audiometria.api.controllers.Batch;

import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

import java.util.UUID;

public class CsvMusicotecaReader extends FlatFileItemReader<Musicoteca> {

    public CsvMusicotecaReader(String filePath, UUID uploadId, String fecha, String euro) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath no puede ser null o vacío");
        }


        setResource(new FileSystemResource(filePath));
        setLinesToSkip(1); // skip header
        setEncoding("UTF-8");

        DefaultLineMapper<Musicoteca> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer() {{
            setNames(
                    "saleStartDate", "saleEndDate", "dsp", "saleStoreName", "saleType", "saleUserType",
                    "territory", "productUpc", "productReference", "productCatalogNumber", "productLabel",
                    "productArtist", "productTitle", "assetArtist", "assetTitle", "assetVersion", "assetDuration",
                    "assetIsrc", "assetReference", "assetProduct", "productQuantity", "assetQuantity",
                    "originalGrossIncome", "originalCurrency", "exchangeRate", "convertedGrossIncome",
                    "contractDealTerm", "reportedRoyalty", "currency", "reportRunId", "reportId", "saleId"
            );
        }});
        lineMapper.setFieldSetMapper(new MusicotecaFieldSetMapper(uploadId,fecha, euro));
        setLineMapper(lineMapper);
    }
}
