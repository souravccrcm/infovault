package com.ccrcm.infovault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@RequestMapping("/infovault")
public class InfovaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfovaultApplication.class, args);
	}

}
