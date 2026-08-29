package com.audiometria.audiometria.api.controllers.reporte;

import com.audiometria.audiometria.api.controllers.request.MusicotecaRoyaltyGroupedExportRequest;
import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.reporte.Reporte;
import com.audiometria.audiometria.api.service.reporte.ExportExcelService;
import com.audiometria.audiometria.api.service.reporte.MusicaSystemService;
import com.audiometria.audiometria.api.service.reporte.TotalesSystemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
@RestController
@RequestMapping("/api/v1/musica")
@Tag(name = "Musica", description = "Musica API V1")
public class MusicaController {

    private final MusicaSystemService musicaSystemService;

    private final TotalesSystemService totalesSystemService;

    private final ExportExcelService exportExcelService;


    public MusicaController(MusicaSystemService musicaSystemService, TotalesSystemService totalesSystemService, ExportExcelService exportExcelService) {
        this.musicaSystemService = musicaSystemService;
        this.totalesSystemService = totalesSystemService;
        this.exportExcelService = exportExcelService;
    }


    /*
        * Endpoint para buscar música en el sistema.
     */
    @PostMapping(value = "/search")
    public Page<Musicoteca> search(@RequestBody SearchRequest request) {
        return musicaSystemService.searchMusicaSystem(request);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,  @RequestParam("userId") UUID userId) {
        musicaSystemService.cargarCsv(file, userId);
        //falta que al cargar se cree un nuevo usuario o busque si existe y el uuid se cree automaticamente
        return ResponseEntity.ok("Archivo procesado correctamente");
    }

    @GetMapping("/totales/{displayName}")
    public ResponseEntity<BigDecimal> obtenerPorcentajeDistribucion(@PathVariable String displayName,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal totales = totalesSystemService.obtenerTotalRoyalty(displayName, startDate, endDate);
        return ResponseEntity.ok(totales);
    }

    /*
     * Endpoint para exportar excel.
     */
    @PostMapping("/exportExcel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestBody SearchRequest request) throws IOException {

        byte[] excel = exportExcelService.generarExcel(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
        );

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("reporte.xlsx")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }

    /*
     * Exportación agrupada.
     */
    @PostMapping("/exportExcelGrouped")
    public ResponseEntity<byte[]> exportExcelGrouped(
            @RequestBody MusicotecaRoyaltyGroupedExportRequest request
    ) throws IOException {

        byte[] excel =
                exportExcelService.generarExcelGrouped(
                        request.getSearchRequest(),
                        request.getGroupBy()
                );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
        );

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(
                                "reporte_"
                                        + request.getGroupBy()
                                        + ".xlsx"
                        )
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }


}
