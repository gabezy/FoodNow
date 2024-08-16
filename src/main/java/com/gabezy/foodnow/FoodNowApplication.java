package com.gabezy.foodnow;

import com.gabezy.foodnow.infra.repositories.GenericRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = GenericRepositoryImpl.class) // dizendo para o spring que essa classe será
                                                                          // a nova implementação do SimpleJpaRepository
public class FoodNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodNowApplication.class, args);
    }

}
