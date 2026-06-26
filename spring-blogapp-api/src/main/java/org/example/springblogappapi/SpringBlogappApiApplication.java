package org.example.springblogappapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBlogappApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBlogappApiApplication.class, args);
    }

}
