package com.moraes.tech_career_pulse;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TechCareerPulseApplication {
	public static void main(String[] args) {SpringApplication.run(TechCareerPulseApplication.class, args);}
}
