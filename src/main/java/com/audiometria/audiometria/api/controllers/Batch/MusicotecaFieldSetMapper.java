package com.audiometria.audiometria.api.controllers.Batch;

import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class MusicotecaFieldSetMapper implements FieldSetMapper<Musicoteca> {

//    private final UUID userId;
    private final UUID uploadId;
    private final LocalDate fecha;

    public MusicotecaFieldSetMapper(UUID uploadId, String fecha) {
        this.uploadId = uploadId;
        this.fecha = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public Musicoteca mapFieldSet(FieldSet fs) {
        Musicoteca m = new Musicoteca();
//        m.setUserId(userId);

        m.setSaleStartDate(parseDate(fs.readString(0)));
        m.setSaleEndDate(parseDate(fs.readString(1)));
        m.setDsp(fs.readString(2));
        m.setSaleStoreName(fs.readString(3));
        m.setSaleType(fs.readString(4));
        m.setSaleUserType(fs.readString(5));
        m.setTerritory(fs.readString(6));
        m.setProductUpc(fs.readString(7));
        m.setProductReference(fs.readString(8));
        m.setProductCatalogNumber(fs.readString(9));
        m.setProductLabel(fs.readString(10));
        m.setProductArtist(fs.readString(11));
        m.setProductTitle(fs.readString(12));
        m.setAssetArtist(fs.readString(13));
        m.setAssetTitle(fs.readString(14));
        m.setAssetVersion(fs.readString(15));
        m.setAssetDuration(fs.readString(16));
        m.setAssetIsrc(fs.readString(17));
        m.setAssetReference(fs.readString(18));
        m.setAssetProduct(fs.readString(19));
        m.setProductQuantity(parseInteger(fs.readString(20)));
        m.setAssetQuantity(parseInteger(fs.readString(21)));
        m.setOriginalGrossIncome(parseBigDecimal(fs.readString(22)));
        m.setOriginalCurrency(fs.readString(23));
        m.setExchangeRate(parseBigDecimal(fs.readString(24)));
        m.setConvertedGrossIncome(parseBigDecimal(fs.readString(25)));
        m.setContractDealTerm(fs.readString(26));
        m.setReportedRoyalty(parseBigDecimal(fs.readString(27)));
        m.setCurrency(fs.readString(28));
        m.setReportRunId(fs.readString(29));
        m.setReportId(fs.readString(30));
        m.setSaleId(fs.readString(31));
        m.setCreatedAt(LocalDateTime.now());
        m.setDateUser(fecha.atStartOfDay());
        m.setState(1L);
        m.setUploadId(uploadId);

        return m;
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
