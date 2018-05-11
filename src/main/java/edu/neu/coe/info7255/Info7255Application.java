package edu.neu.coe.info7255;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan
public class Info7255Application {

	public static void main(String[] args) {
		SpringApplication.run(Info7255Application.class, args);
	}

}
