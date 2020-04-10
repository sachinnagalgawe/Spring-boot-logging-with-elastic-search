package com.example.elasticdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ElasticDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticDemoApplication.class, args);
	}
}
