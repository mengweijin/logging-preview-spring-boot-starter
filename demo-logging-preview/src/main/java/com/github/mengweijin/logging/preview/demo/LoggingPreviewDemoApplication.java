package com.github.mengweijin.logging.preview.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author mengweijin
 */
@EnableScheduling
@SpringBootApplication
public class LoggingPreviewDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoggingPreviewDemoApplication.class, args);
	}
}
