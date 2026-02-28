package com.audiometria.audiometria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AudiometriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudiometriaApplication.class, args);
	}

}
