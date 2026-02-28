package com.audiometria.audiometria.api.service.upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final JobLauncher asyncJobLauncher;
    private final Job job;

    @Async
    public CompletableFuture<Void> processZipAsync(String zipPath, String fecha, String valorEuro) {

        Path savedZip = Path.of(zipPath);
        Path extractedDir = null;

        try {
            log.info("Iniciando procesamiento async para {}", zipPath);

            // 1️⃣ Crear carpeta destino
            extractedDir = Paths.get(zipPath.replace(".zip", ""));
            Files.createDirectories(extractedDir);

            // 2️⃣ Descomprimir
            unzip(savedZip.toString(), extractedDir.toString());

            // 3️⃣ Buscar archivo
            File fileToProcess = findFileToProcess(extractedDir.toFile());

            if (fileToProcess == null) {
                throw new IllegalStateException("No se encontró archivo procesable");
            }

            // 4️⃣ Lanzar Batch
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", fileToProcess.getAbsolutePath())
                    .addString("uploadId", UUID.randomUUID().toString())
                    .addString("fecha", fecha)
                    .addString("valorEuro", valorEuro)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            asyncJobLauncher.run(job, jobParameters);

            log.info("Job lanzado correctamente");

        } catch (Exception e) {
            log.error("Error procesando ZIP", e);
            return CompletableFuture.failedFuture(e);
        } finally {
            // 🧹 Limpieza COMPLETA
            try {
                if (savedZip != null) Files.deleteIfExists(savedZip);

                if (extractedDir != null && Files.exists(extractedDir)) {
                    deleteDirectoryRecursively(extractedDir);
                    System.out.println("borrando");
                }

                log.info("Archivos temporales eliminados");
            } catch (Exception ex) {
                log.error("Error limpiando archivos", ex);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    private void unzip(String zipFilePath, String destDir) throws IOException {

        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        byte[] buffer = new byte[1024];

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {

            File newFile = new File(destDir + File.separator + zipEntry.getName());

            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    private File findFileToProcess(File directory) {

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".csv")) {
                return file;
            }
        }

        throw new RuntimeException("No se encontró archivo CSV para procesar");
    }

    private void deleteDirectoryRecursively(Path path) throws IOException {

        if (path == null || !Files.exists(path)) {
            return;
        }

        Files.walk(path)
                .sorted(Comparator.reverseOrder()) // 🔥 primero archivos hijos
                .forEach(currentPath -> {
                    try {
                        Files.deleteIfExists(currentPath);
                    } catch (IOException e) {
                        throw new RuntimeException("Error eliminando: " + currentPath, e);
                    }
                });
    }

}
