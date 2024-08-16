package com.gabezy.foodnow.controllers;

import com.gabezy.foodnow.domain.entity.Restaurant;

import com.gabezy.foodnow.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TesteController {

    private final RestaurantRepository restaurantRepository;


    @GetMapping("/restaurants/free-delivery")
    public List<Restaurant> restaurantsFreeDeliveryFee (@RequestParam String name) {
        return restaurantRepository.findWithFreeDeliveryFee(name);
    } // Usando a specification do DDD para realizar consultadas com predicates

    @GetMapping("restaurants/first")
    public Optional<Restaurant> restaurantFirst() {
        return restaurantRepository.findFirst();
    }

}
