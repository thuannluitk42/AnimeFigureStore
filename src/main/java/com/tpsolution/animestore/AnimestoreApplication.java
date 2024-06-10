package com.tpsolution.animestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnimestoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimestoreApplication.class, args);
	}

}
