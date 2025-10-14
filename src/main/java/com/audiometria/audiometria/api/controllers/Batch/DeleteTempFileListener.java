package com.audiometria.audiometria.api.controllers.Batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DeleteTempFileListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {}

    @Override
    public void afterJob(JobExecution jobExecution) {
        String filePath = jobExecution.getJobParameters().getString("fullPathFileName");
        if (filePath != null && !filePath.isBlank()) {
            try {
                Path path = Paths.get(filePath);
                boolean deleted = Files.deleteIfExists(path);
                if (deleted) {
                    System.out.println("Archivo temporal eliminado: " + filePath);
                } else {
                    System.out.println("El archivo no existía: " + filePath);
                }
            } catch (IOException e) {
                System.err.println("No se pudo eliminar el archivo: " + filePath + " → " + e.getMessage());
            }
        }
    }
}
