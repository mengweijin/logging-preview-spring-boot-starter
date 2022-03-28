package com.github.mengweijin.logging.previewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mengweijin
 */
@ComponentScan(basePackages = "com.github.mengweijin")
@SpringBootApplication
public class LoggingPreviewerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoggingPreviewerApplication.class, args);
	}
}
