package com.gabezy.foodnow.infra.repositories;

import com.gabezy.foodnow.domain.entity.Restaurant;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantRepositoryCustomized {
    List<Restaurant> findCustomized(String name, BigDecimal minDeliveryFee, BigDecimal maxDeliveryFee);
    List<Restaurant> findWithFreeDeliveryFee(String name);
}
