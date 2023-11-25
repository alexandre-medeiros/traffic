package com.alexmart.traffic;

import com.alexmart.traffic.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class TrafficApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrafficApiApplication.class, args);
    }

}
