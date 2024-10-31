package com.gabezy.foodnow.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private Long id;

    private String name;

    private BigDecimal deliveryFee;

    private CuisineDTO cuisine;

}
