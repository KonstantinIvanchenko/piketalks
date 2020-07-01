package com.piketalks.beservicejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BeserviceJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeserviceJavaApplication.class, args);
	}

}
