package com.audiometria.audiometria.api.controllers.reporte;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.reporte.Reporte;
import com.audiometria.audiometria.api.service.reporte.MusicaSystemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/musica")
@Tag(name = "Musica", description = "Musica API V1")
public class MusicaController {

    private final MusicaSystemService musicaSystemService;

    public MusicaController(MusicaSystemService musicaSystemService) {
        this.musicaSystemService = musicaSystemService;
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




}
