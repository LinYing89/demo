package com.example.demo;

import com.example.demo.data.Config;
import com.example.demo.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@Autowired
	public Config createConfig(ConfigRepository configRepository){
        return configRepository.getOne(1L);
	}
}
