package com.gabezy.foodnow.exceptions;

public class RestaurantNotFoundException extends EntityNotFoundException{

    private static final String ERRO_MSG = "Restaurant not found. ID: ";

    public RestaurantNotFoundException(Long id) {
        super(ERRO_MSG + id);
    }
}
