package com.hiper2d;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hiper2d.terminal.TerminalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class ClientRunner implements CommandLineRunner {

	@Autowired
	TerminalManager terminalManager;

	@Override
	public void run(String... args) throws Exception {
		terminalManager.start();
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		SpringApplication.run(ClientRunner.class, args).close();
	}

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializationInclusion(JsonInclude.Include.NON_NULL);
		return builder;
	}
}
