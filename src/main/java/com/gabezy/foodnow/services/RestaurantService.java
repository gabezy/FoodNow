package com.gabezy.foodnow.services;

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

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
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

    public Restaurant update(Long id, Restaurant input) {
            var restaurant = findById(id);
            // copia a propriedades de um objeto para o outro, sendo possível ignorar propriedades específicas
            BeanUtils.copyProperties(input, restaurant, "id", "paymentMethods", "address", "createdAt");
            return save(restaurant);
    }

}
