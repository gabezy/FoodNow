package com.gabezy.foodnow.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class EntityNotFoundException extends  BusinessException{

    private static final String ERRO_MSG = "Entity not found";

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException() {
        this(ERRO_MSG);
    }

}
