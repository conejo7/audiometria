package com.audiometria.audiometria.api.controllers.Batch;


import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.musicoteca.MusicotecaRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.UUID;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MusicotecaRepository musicotecaRepository;
    private final DeleteTempFileListener deleteTempFileListener;

    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       MusicotecaRepository musicotecaRepository, DeleteTempFileListener deleteTempFileListener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.musicotecaRepository = musicotecaRepository;
        this.deleteTempFileListener = deleteTempFileListener;
    }

    @Bean
    public Job csvMusicotecaJob(Step step1) {
        return new JobBuilder("csvMusicotecaJob", jobRepository) // ← CORREGIDO
                .incrementer(new RunIdIncrementer())
                .listener(deleteTempFileListener) // ← clase separada
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(ItemReader<Musicoteca> reader,
                      ItemProcessor<Musicoteca, Musicoteca> processor,
                      ItemWriter<Musicoteca> writer) {
        return new StepBuilder("step1", jobRepository) // ← CORREGIDO
                .<Musicoteca, Musicoteca>chunk(300, transactionManager) // ← CORREGIDO
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Musicoteca> reader(@Value("#{jobParameters['fullPathFileName']}") String pathToFile,
                                                 @Value("#{jobParameters['fecha']}") String fecha,
                                                 @Value("#{jobParameters['uploadId']}") String uploadId,
                                                 @Value("#{jobParameters['valorEuro']}") String valorEuro) {

        System.out.println("🟢 pathToFile: " + pathToFile);
//        System.out.println("🟢 userId: " + userId);

//        if (pathToFile == null || pathToFile.isBlank()) {
//            throw new IllegalArgumentException("pathToFile es null o vacío");
//        }
//
//        if (valorEuro == null || valorEuro.isBlank()) {
//            throw new IllegalArgumentException("valorEuro es null o vacío");
//        }
        if (pathToFile.isBlank() || valorEuro.isBlank() || uploadId.isBlank()) {
            // No lanzar excepción en el arranque!
            System.out.println("⏳ Batch aún no ejecutado: parámetros vacíos al iniciar.");
            return new FlatFileItemReader<>(); // lector vacío hasta que se lance el job
        }

        return new CsvMusicotecaReader(pathToFile, UUID.fromString(uploadId), fecha, valorEuro);
    }

    @Bean
    public ItemProcessor<Musicoteca, Musicoteca> processor() {
        return item -> item; // opcional para validaciones extra
    }

    @Bean
    public ItemWriter<Musicoteca> writer() {
        return items -> musicotecaRepository.saveAll(items);
    }



}
