package com.gabezy.foodnow.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabezy.foodnow.domain.entity.Restaurant;
import com.gabezy.foodnow.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findById(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> findAll() {
        return ResponseEntity.ok(restaurantRepository.findAll());
    }

    @GetMapping("/by-delivery-fee")
    public ResponseEntity<List<Restaurant>> findByDeliveryFee(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) BigDecimal minDeliveryFee,
                                                              @RequestParam (required = false) BigDecimal maxDeliveryFee) {
        return ResponseEntity.ok(restaurantRepository.findCustomized(name, minDeliveryFee, maxDeliveryFee));
    }

    @GetMapping("/by-name-and-cuisine-id")
    public ResponseEntity<List<Restaurant>> findByNameAndCuisineId(@RequestParam String name, @RequestParam Long cuisineId) {
        return ResponseEntity.ok(restaurantRepository.consultarPorNome(name, cuisineId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Restaurant restaurant, UriComponentsBuilder builder) {
        try {
            var restaurantSaved = restaurantRepository.save(restaurant);
            var uri = builder.path("/restaurants/{id}").buildAndExpand(restaurantSaved.getId()).toUri();
            return ResponseEntity.created(uri).body(restaurantSaved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        try {
            var restaurantSaved = restaurantRepository.findById(id).get();
            BeanUtils.copyProperties(restaurant, restaurantSaved, "id");
            return ResponseEntity.ok(restaurantRepository.save(restaurantSaved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        var restaurantSaved = restaurantRepository.findById(id).get();
        this.merge(fields, restaurantSaved);
        restaurantRepository.save(restaurantSaved);
        return ResponseEntity.ok(restaurantSaved);
    }

    private void merge(Map<String, Object> fields, Restaurant restaurant) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurant restaurantOrigin = objectMapper.convertValue(fields, Restaurant.class);
        logger.info("merging restaurant " + restaurantOrigin.getName());
        logger.info("merging restaurant " + restaurantOrigin.getDeliveryFee());
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Restaurant.class, key);
            if (field != null) {
                field.setAccessible(true);
                Object newValue = ReflectionUtils.getField(field, restaurantOrigin);
                ReflectionUtils.setField(field, restaurant, newValue);
            }
        });
    }
}
