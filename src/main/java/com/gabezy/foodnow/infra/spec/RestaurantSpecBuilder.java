package com.gabezy.foodnow.infra.spec;

import com.gabezy.foodnow.domain.entity.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestaurantSpecBuilder {

    public static Specification<Restaurant> withFreeDelivery () {
        return ((root, query, builder) -> builder.equal(root.get("deliveryFee"), BigDecimal.ZERO));
    }

    public static Specification<Restaurant> withNameLike(String name) {
        return ((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
    }

}
