package com.gabezy.foodnow;

import com.gabezy.foodnow.infra.repositories.GenericRepositoryImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = GenericRepositoryImpl.class) // dizendo para o spring que essa classe será
                                                                          // a nova implementação do SimpleJpaRepository
public class FoodNowApplication {

    @PostConstruct
    public void initAll() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodNowApplication.class, args);
    }

}
