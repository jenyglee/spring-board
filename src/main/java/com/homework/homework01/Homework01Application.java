package com.homework.homework01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Homework01Application {

    public static void main(String[] args) {
        SpringApplication.run(Homework01Application.class, args);
    }

}
