package com.gabezy.foodnow.repositories;

import com.gabezy.foodnow.domain.entity.Restaurant;
import com.gabezy.foodnow.infra.repositories.RestaurantRepositoryCustomized;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;


public interface RestaurantRepository
        extends GenericRepository<Restaurant, Long>, RestaurantRepositoryCustomized,
        JpaSpecificationExecutor<Restaurant> {
    List<Restaurant> findByDeliveryFeeBetween(BigDecimal minDeliveryFee, BigDecimal maxDeliveryFee);
    List<Restaurant> findByNameContainingAndCuisineId(String name, Long cuisineId);
//    @Query("from Restaurant where name like %:nome% and cuisine.id = :id")
    List<Restaurant> consultarPorNome(String nome, Long id);
}
