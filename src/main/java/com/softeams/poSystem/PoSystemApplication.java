package com.softeams.poSystem;

import com.softeams.poSystem.security.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class PoSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoSystemApplication.class, args);
	}

}
