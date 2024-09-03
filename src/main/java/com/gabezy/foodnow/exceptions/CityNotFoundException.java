package com.gabezy.foodnow.exceptions;

public class CityNotFoundException extends EntityNotFoundException{

    private static final String ERRO_MSG = "City not found. ID: ";

    public CityNotFoundException(Long id) {
        super(ERRO_MSG + id);
    }
}
