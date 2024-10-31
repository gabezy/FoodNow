package com.gabezy.foodnow.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabezy.foodnow.domain.dto.RestaurantDTO;
import com.gabezy.foodnow.domain.entity.Restaurant;
import com.gabezy.foodnow.repositories.RestaurantRepository;
import com.gabezy.foodnow.services.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private final SmartValidator validator;

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> findAll() {
        return ResponseEntity.ok(restaurantRepository.findAll());
    }

    @GetMapping("/by-delivery-fee")
    public ResponseEntity<List<Restaurant>> findByDeliveryFee(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) BigDecimal minDeliveryFee,
                                                              @RequestParam(required = false) BigDecimal maxDeliveryFee) {
        return ResponseEntity.ok(restaurantRepository.findCustomized(name, minDeliveryFee, maxDeliveryFee));
    }

    @GetMapping("/by-name-and-cuisine-id")
    public ResponseEntity<List<Restaurant>> findByNameAndCuisineId(@RequestParam String name, @RequestParam Long cuisineId) {
        return ResponseEntity.ok(restaurantRepository.consultarPorNome(name, cuisineId));
    }

    @PostMapping
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant input, UriComponentsBuilder builder) {
        var restaurant = restaurantService.save(input);
        var uri = builder.path("restaurants/{id}").buildAndExpand(restaurant.getId()).toUri();
        return ResponseEntity.created(uri).body(restaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.update(id, restaurant));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Restaurant> patch(@PathVariable Long id, @RequestBody Map<String, Object> fields, HttpServletRequest request) throws MethodArgumentNotValidException {
        var restaurantSaved = restaurantRepository.findById(id).get();
        this.merge(fields, restaurantSaved, request);
        validate(restaurantSaved, "restaurant");
        restaurantRepository.save(restaurantSaved);
        return ResponseEntity.ok(restaurantSaved);
    }

    private void merge(Map<String, Object> fields, Restaurant restaurant, HttpServletRequest request) {
        var serverHttpRequest = new ServletServerHttpRequest(request);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            Restaurant restaurantOrigin = objectMapper.convertValue(fields, Restaurant.class);
            log.info("merging restaurant {}", restaurantOrigin.getName());
            log.info("merging restaurant {}", restaurantOrigin.getDeliveryFee());
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Restaurant.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    Object newValue = ReflectionUtils.getField(field, restaurantOrigin);
                    ReflectionUtils.setField(field, restaurant, newValue);
                }
            });
        } catch (IllegalArgumentException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest );
        }
    }

    private void validate(Restaurant restaurant, String objectName) throws MethodArgumentNotValidException {
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(restaurant, objectName);

        validator.validate(restaurant, beanPropertyBindingResult);

        if (beanPropertyBindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(getMergeMethodParameter(), beanPropertyBindingResult);
        }
    }

    private MethodParameter getMergeMethodParameter() {
            var classMethod = Arrays.stream(this.getClass().getDeclaredMethods())
                    .filter(method -> method.getName().equalsIgnoreCase("patch"))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchMethodError("methid not found"));
            return new MethodParameter(classMethod, -1);
    }
}
