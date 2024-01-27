package com.example.moviesdb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MoviesDbApplication {


	public static void main(String[] args) {
		SpringApplication.run(MoviesDbApplication.class, args);
	}


}
