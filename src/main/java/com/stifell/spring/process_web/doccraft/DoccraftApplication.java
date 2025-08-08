package com.stifell.spring.process_web.doccraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DoccraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoccraftApplication.class, args);
	}

}
