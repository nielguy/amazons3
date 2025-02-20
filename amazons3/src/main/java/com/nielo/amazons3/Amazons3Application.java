package com.nielo.amazons3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Amazons3Application {

	public static void main(String[] args) {
		SpringApplication.run(Amazons3Application.class, args);
	}

}
