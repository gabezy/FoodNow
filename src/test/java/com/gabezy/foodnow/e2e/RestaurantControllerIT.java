package com.gabezy.foodnow.e2e;

import com.gabezy.foodnow.config.exceptionhandler.ResponseError;
import com.gabezy.foodnow.domain.entity.Cuisine;
import com.gabezy.foodnow.domain.entity.Restaurant;
import com.gabezy.foodnow.repositories.RestaurantRepository;
import com.gabezy.foodnow.services.CuisineService;
import com.gabezy.foodnow.util.DatabaseCleaner;
import com.gabezy.foodnow.util.RestRequestUtils;
import com.gabezy.foodnow.util.RestRequestUtils.RestFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Slf4j
class RestaurantControllerIT {

    @Autowired
    private RestRequestUtils restRequestUtils;

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private DatabaseCleaner dbCleaner;

    private Cuisine cuisine = null;

    private static final String[] RESTAURANTS_NAMES = {"MC Donalds", "Wendy's", "Nativus", "Starbucks", "Hard rock café"};

    private static final String BASE_ENDPOINT = "/restaurants";

    private static final int VALID_ID = 1;

    private static final int INVALID_ID = 999;


    @BeforeEach
    public void setupEach() {
        dbCleaner.clearTables();
        cuisine = createCuisine();
    }

    @Test
    void should_ReturnAllRestaurant_WhenGetting() {
        populateDatabase(cuisine);

        var filter = RestFilter.builder().get(true).endpoint(BASE_ENDPOINT).build();
        var response = restRequestUtils.buildRequestRest(filter);

        assertEquals(HttpStatus.OK.value(), response.statusCode());

        List<Restaurant> responseBody = response.jsonPath().getList("", Restaurant.class);

        boolean hasSameCuisineId = responseBody.stream()
                .allMatch(restaurant -> Objects.equals(restaurant.getCuisine().getId(), cuisine.getId()));

        assertEquals(RESTAURANTS_NAMES.length, responseBody.size());
        assertTrue(hasSameCuisineId);
    }

    @Test
    void should_ReturnRestaurant_WhenGettingByExistentId() {
        populateDatabase(cuisine);

        var filter = RestFilter.builder().get(true).endpoint(BASE_ENDPOINT + "/" + VALID_ID).build();
        var response = restRequestUtils.buildRequestRest(filter);

        assertEquals(HttpStatus.OK.value(), response.statusCode());

        Restaurant responseBody = response.jsonPath().getObject("", Restaurant.class);

        assertEquals(RESTAURANTS_NAMES[VALID_ID - 1], responseBody.getName());
        assertEquals(0, BigDecimal.TEN.compareTo(responseBody.getDeliveryFee()));
    }

    @Test
    void should_ReturnNotFound_WhenGettingByNonExistentId() {
        var filter = RestFilter.builder().get(true).endpoint(BASE_ENDPOINT + "/" + INVALID_ID).build();
        var response = restRequestUtils.buildRequestRest(filter);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());

        ResponseError error = response.jsonPath().getObject("", ResponseError.class);

        assertTrue(error.getDetail().toLowerCase().contains("restaurant not found"));
    }

    private Cuisine createCuisine() {
        var cuisine1 = new Cuisine();
        cuisine1.setName("Fast Food");
        return cuisineService.save(cuisine1);
    }

    private void populateDatabase(Cuisine cuisine) {
        List<Restaurant> restaurantList = new ArrayList<>();
        Arrays.asList(RESTAURANTS_NAMES).forEach(restaurantName -> {
            Restaurant restaurant = new Restaurant();
            restaurant.setCuisine(cuisine);
            restaurant.setName(restaurantName);
            restaurant.setDeliveryFee(BigDecimal.TEN);
            restaurantList.add(restaurant);
        });

        repository.saveAll(restaurantList);
    }
}
