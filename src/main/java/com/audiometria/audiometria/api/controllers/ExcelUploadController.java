package com.audiometria.audiometria.api.controllers;

import com.audiometria.audiometria.api.repository.dto.MusicExcelDTO;
import com.audiometria.audiometria.api.service.ExcelImportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelUploadController {

    @Autowired
    private ExcelImportService excelImportService;

    //ya no se usa
    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try(InputStream is = file.getInputStream()) {
            List<MusicExcelDTO> registros = leerExcel(is);
            excelImportService.importarDatos(registros);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }

    }


    private List<MusicExcelDTO> leerExcel(InputStream is) throws IOException {
        List<MusicExcelDTO> lista = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        // Saltar la cabecera
        if (rowIterator.hasNext()) rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            MusicExcelDTO dto = new MusicExcelDTO();
            dto.setUsuario(getCellString(row.getCell(0)));
            dto.setTitle(getCellString(row.getCell(1)));
            dto.setArtist(getCellString(row.getCell(2)));
            dto.setAmountEur(getCellBigDecimal(row.getCell(3)));

            lista.add(dto);
        }

        workbook.close();
        return lista;
    }

    private String getCellString(Cell cell) {
        return cell != null ? cell.toString().trim() : null;
    }

    private BigDecimal getCellBigDecimal(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        try {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } catch (Exception e) {
            return new BigDecimal(cell.getStringCellValue());
        }
    }

}
