package com.example.gae.spring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.example.gae.spring.push")
public class SpringBootWebApplication {
		public static void main(String[] args) {
                SpringApplication.run(SpringBootWebApplication.class, args);
        }
}