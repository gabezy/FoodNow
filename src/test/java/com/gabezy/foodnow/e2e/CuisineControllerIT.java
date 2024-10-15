package com.gabezy.foodnow.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabezy.foodnow.config.exceptionhandler.ErrorType;
import com.gabezy.foodnow.config.exceptionhandler.ResponseError;
import com.gabezy.foodnow.domain.entity.Cuisine;
import com.gabezy.foodnow.repositories.CuisineRepository;
import com.gabezy.foodnow.util.DatabaseCleaner;
import com.gabezy.foodnow.util.RestRequestUtils;
import com.gabezy.foodnow.util.RestRequestUtils.RestFilter;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class CuisineControllerIT {

    @Autowired
    private RestRequestUtils requestRestUtils;

    @Autowired
    private CuisineRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DatabaseCleaner dbCleaner;

    private static final String[] CUISINES_NAMES = {"Indian", "Brazilian", "Japanese", "German", "Spanish",
            "Mexican", "Italian"};

    private static final String ENDPOINT = "/cuisines";

    private static final int ID_NON_EXISTENT = 999;


    @BeforeAll
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        dbCleaner.clearTables();
    }


    @Test
    void should_ReturnAllCuisines_WhenGettingCuisines() {
        populateDatabase();

        var filter = RestFilter.builder().get(true).endpoint(ENDPOINT).build();
        var response = requestRestUtils.buildRequestRest(filter);

        assertEquals(200, response.statusCode(), "Status code should be 200 (OK)");

        List<Object> body = response.jsonPath().get();
        List<Cuisine> cuisines = objectMapper.convertValue(body,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Cuisine.class));

        assertEquals(CUISINES_NAMES.length, cuisines.size());

        boolean found = cuisines.stream()
                .anyMatch(cuisine -> cuisine.getName().equalsIgnoreCase("indian") &&
                        cuisine.getId() == 1);

        assertTrue(found);
    }

    @Test
    void should_ReturnCreatedStatus_WhenCreatingCuisine() {
        var cuisine = new Cuisine();
        String cuisineName = "Australian";
        cuisine.setName(cuisineName);
        var filter = RestFilter.builder().body(cuisine).post(true).endpoint(ENDPOINT).build();

        var response = requestRestUtils.buildRequestRest(filter);

        assertEquals(201, response.statusCode());

        Cuisine responseBody = objectMapper.convertValue(response.jsonPath().get(), Cuisine.class);

        assertEquals(responseBody.getName(), cuisineName);
        assertEquals(responseBody.getId().getClass(), Long.class);
    }

    @Test
    void should_ReturnCuisine_WhenGettingByExistentId() {
        populateDatabase();

        int id = 1;
        var filter = RestFilter.builder().get(true).endpoint(ENDPOINT + "/" + id).build();
        var response = requestRestUtils.buildRequestRest(filter);

        assertEquals(200, response.statusCode());

        Cuisine responseBody = objectMapper.convertValue(response.jsonPath().get(), Cuisine.class);

        String cuisineName = CUISINES_NAMES[id - 1];

        assertEquals(cuisineName, responseBody.getName());
    }

    @Test
    void should_ReturnNotFound_WhenGettingByNonExistentId() {
        var filter = RestFilter.builder().get(true).endpoint(ENDPOINT + "/" + ID_NON_EXISTENT).build();
        var response = requestRestUtils.buildRequestRest(filter);

        assertEquals(404, response.statusCode());

        ResponseError responseBody = objectMapper.convertValue(response.jsonPath().get(), ResponseError.class);

        assertEquals(ErrorType.RESOURCE_NOT_FOUND.getTitle(), responseBody.getTitle());
        assertEquals(404, responseBody.getStatus());
    }

    private void populateDatabase() {
        int length = CUISINES_NAMES.length;
        Cuisine[] cuisines = new Cuisine[length];
        for (int i = 0; i < length; i++) {
            var cuisine = new Cuisine();
            cuisine.setName(CUISINES_NAMES[i]);
            cuisines[i] = cuisine;
        }

        repository.saveAll(Arrays.asList(cuisines));
    }
}
