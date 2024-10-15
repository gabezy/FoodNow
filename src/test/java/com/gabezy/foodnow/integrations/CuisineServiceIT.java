package com.gabezy.foodnow.integrations;

import com.gabezy.foodnow.domain.entity.Cuisine;
import com.gabezy.foodnow.exceptions.CuisineNotFoundException;
import com.gabezy.foodnow.repositories.CuisineRepository;
import com.gabezy.foodnow.services.CuisineService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CuisineServiceIT {

    @Autowired
    private CuisineService service;

    @Autowired
    private CuisineRepository repository;

    @Test
    void should_RegisterCuisine_WhenGivenValidData() {
        // Scenario
        var cuisine = new Cuisine();
        cuisine.setName("Chinese");
        // Action
        cuisine = service.save(cuisine);
        // Validation
        assertNotNull(cuisine);
        assertTrue(cuisine.getId() > 0);

    }

    @Test
    void should_ThrowException_WhenRegisteringCuisingWithoutName() {
        var cuisine = new Cuisine();
        cuisine.setName(null);

        assertThrows(ConstraintViolationException.class, () -> service.save(cuisine));
    }

    @Test
    void should_DeleteCuisineById_WhenGivenValidId() {
        var cuisine = new Cuisine();
        cuisine.setName("German");
        cuisine = service.save(cuisine);
        Long cuisineId = cuisine.getId();

        service.deleteById(cuisineId);

        Optional<Cuisine> deletedCuisine = repository.findById(cuisineId);
        assertTrue(deletedCuisine.isEmpty(), "Cuisine should have been deleted");

    }

    @Test
    void should_ThrowException_WhenDeleteCuisineInUse() {

    }

    @Test
    void should_ThrowException_WhenDeleteNonExistentCuisine() {
        Long id = 999L;
        assertThrows(CuisineNotFoundException.class, () -> service.deleteById(id));
    }


}
