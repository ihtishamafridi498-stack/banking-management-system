package com.afridi.bankmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BankmanagementsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankmanagementsystemApplication.class, args);
	}

}
