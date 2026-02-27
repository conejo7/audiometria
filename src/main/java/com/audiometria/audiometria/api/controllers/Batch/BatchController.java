package com.audiometria.audiometria.api.controllers.Batch;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
@RestController
@RequestMapping("/api/v1/musica/batch")
@Tag(name = "Musica", description = "Musica API V1")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("csvMusicotecaJob")  // nombre del bean
    private Job job;


    //este se usa
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
//                                    @RequestParam("userId") UUID userId,
                                    @RequestParam("fecha") String fecha,
                                    @RequestParam("valorEuro") String valorEuro ) throws Exception {

        System.out.println("Iniciando upload");
        Path tempDir = Files.createTempDirectory("upload-");
        File tempFile = new File(tempDir.toFile(), file.getOriginalFilename());
        file.transferTo(tempFile);

        LocalDate fechaLocal = LocalDate.parse(fecha);
        UUID uploadId = UUID.randomUUID();

        // Aquí deberías tener tu Job configurado, por ejemplo:
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", tempFile.getAbsolutePath())
//                .addString("userId", userId.toString())
                .addString("uploadId", uploadId.toString())
                .addString("fecha", fecha)
                .addString("valorEuro", valorEuro)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job, jobParameters);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Job lanzado correctamente");
        response.put("fileName", file.getOriginalFilename());
        response.put("fecha", fecha);

        System.out.println("Finalizando upload");
        return ResponseEntity.ok(response);
    }

 //subir archivo a volumen railway
    @PostMapping("/upload-zip")
    public ResponseEntity<?> uploadZip(
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        System.out.println("Iniciando upload-zip");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Archivo vacío");
        }

        // Validar que sea zip
        if (!file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
            return ResponseEntity.badRequest().body("Solo se permiten archivos .zip");
        }

        // Crear carpeta dentro del volume
        File uploadDir = new File("/mnt/uploads");

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Nombre único para evitar sobreescritura
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File savedFile = new File(uploadDir, fileName);

        // Guardar archivo en volume
        file.transferTo(savedFile);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Archivo subido correctamente al volume",
                        "fileName", fileName,
                        "path", savedFile.getAbsolutePath()
                )
        );
    }

        @GetMapping("/debug/list-volume")
public ResponseEntity<?> listVolume() {

    File dir = new File("/mnt/uploads");

    if (!dir.exists()) {
        return ResponseEntity.ok("No existe /mnt/uploads");
    }

    return ResponseEntity.ok(dir.list());
}
        
}
