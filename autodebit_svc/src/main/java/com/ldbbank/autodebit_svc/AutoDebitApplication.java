package com.ldbbank.autodebit_svc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AutoDebitApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(AutoDebitApplication.class, args);
		System.out.printf("start project is ok let go ====>");
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
