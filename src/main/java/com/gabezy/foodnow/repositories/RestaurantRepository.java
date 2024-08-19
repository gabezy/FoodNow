package com.gabezy.foodnow.repositories;

import com.gabezy.foodnow.domain.entity.Restaurant;
import com.gabezy.foodnow.infra.repositories.RestaurantRepositoryCustomized;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;


public interface RestaurantRepository
        extends GenericRepository<Restaurant, Long>, RestaurantRepositoryCustomized,
        JpaSpecificationExecutor<Restaurant> {
    @Query("FROM Restaurant r JOIN FETCH r.cuisine LEFT JOIN FETCH r.paymentMethods")
    List<Restaurant> findAll();
    // sobrescrevendo o método findAll para remover o problema de n+1 queries usando JOINs
    List<Restaurant> findByDeliveryFeeBetween(BigDecimal minDeliveryFee, BigDecimal maxDeliveryFee);
    List<Restaurant> findByNameContainingAndCuisineId(String name, Long cuisineId);
//    @Query("from Restaurant where name like %:nome% and cuisine.id = :id")
    List<Restaurant> consultarPorNome(String nome, Long id);
}
