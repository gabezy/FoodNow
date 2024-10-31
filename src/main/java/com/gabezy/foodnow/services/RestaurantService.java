package com.gabezy.foodnow.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabezy.foodnow.domain.dto.CuisineDTO;
import com.gabezy.foodnow.domain.dto.RestaurantDTO;
import com.gabezy.foodnow.domain.entity.Restaurant;
import com.gabezy.foodnow.exceptions.BusinessException;
import com.gabezy.foodnow.exceptions.EntityNotFoundException;
import com.gabezy.foodnow.exceptions.RestaurantNotFoundException;
import com.gabezy.foodnow.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CuisineService cuisineService;
    private final ObjectMapper objectMapper;

    public RestaurantDTO findById(Long id) {
        var restaurant = findRestaurant(id);
        return mapToRestaurantDTO(restaurant);
    }

    public Restaurant save(Restaurant input) {
        try {
            var cuisine = cuisineService.findById(input.getCuisine().getId());
            input.setCuisine(cuisine);
            return restaurantRepository.save(input);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public RestaurantDTO update(Long id, Restaurant input) {
            var restaurant = findRestaurant(id);
            // copia a propriedades de um objeto para o outro, sendo possível ignorar propriedades específicas
            BeanUtils.copyProperties(input, restaurant, "id", "paymentMethods", "address", "createdAt");
            restaurantRepository.save(restaurant);
            return mapToRestaurantDTO(restaurant);
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    private RestaurantDTO mapToRestaurantDTO(Restaurant restaurant) {
        CuisineDTO cuisineDTO = objectMapper.convertValue(restaurant.getCuisine(), CuisineDTO.class);
        RestaurantDTO restaurantDTO = objectMapper.convertValue(restaurant, RestaurantDTO.class);
        restaurantDTO.setCuisine(cuisineDTO);
        return restaurantDTO;
    }

}
