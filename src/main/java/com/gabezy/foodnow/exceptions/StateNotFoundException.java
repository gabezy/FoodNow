package com.gabezy.foodnow.exceptions;

public class StateNotFoundException extends EntityNotFoundException {

    private static final String ERRO_MSG = "State not found. ID: ";

    public StateNotFoundException(Long id) {
        super(ERRO_MSG + id);
    }

}
