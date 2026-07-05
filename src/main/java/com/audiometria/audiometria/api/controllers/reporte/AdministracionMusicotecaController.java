package com.audiometria.audiometria.api.controllers.reporte;


import com.audiometria.audiometria.api.service.administracion.AdministracionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
@RestController
@RequestMapping("/api/v1/musica")
@Tag(name = "Administracion", description = "Musica API V1")
public class AdministracionMusicotecaController {

    private final AdministracionService musicaService;

    public AdministracionMusicotecaController(AdministracionService musicaService) {
        this.musicaService = musicaService;
    }

    @PostMapping("/refresh-materialized-view")
    public ResponseEntity<String> refreshMaterializedView() {

        log.info("Ingresando a Actualizar vista: {}", LocalDateTime.now());
        musicaService.refreshMaterializedView();
        log.info("Finalizando Actualizar vista: {}", LocalDateTime.now());
        return ResponseEntity.accepted().body("La actualización de la vista materializada se inició.");

    }

}
