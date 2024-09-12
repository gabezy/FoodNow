package com.gabezy.foodnow.exceptions;

public abstract class EntityNotFoundException extends  BusinessException{

    private static final String ERRO_MSG = "Entity not found";

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException() {
        this(ERRO_MSG);
    }

}
