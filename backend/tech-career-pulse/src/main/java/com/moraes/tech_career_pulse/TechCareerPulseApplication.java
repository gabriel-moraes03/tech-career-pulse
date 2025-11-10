package com.moraes.tech_career_pulse;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TechCareerPulseApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().directory("../../").load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(TechCareerPulseApplication.class, args);
	}

}
