package com.gabezy.foodnow.exceptions;

public class EntityInUseException extends RuntimeException{

    public EntityInUseException(String message) {
        super(message);
    }

}
