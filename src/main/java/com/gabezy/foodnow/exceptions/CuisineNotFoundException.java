package com.gabezy.foodnow.exceptions;

public class CuisineNotFoundException extends EntityNotFoundException{

    private static final String ERRO_MSG = "Cuisine not found. ID: ";

    public CuisineNotFoundException(Long id) {
        super(ERRO_MSG + id);
    }
}
