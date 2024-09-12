package com.gabezy.foodnow.config.exceptionhandler;

import lombok.Getter;

@Getter
public enum ErrorType {

    BAD_REQUEST("/bad-request", "Bad request"),
    CONFLICT("/conflict", "Entity in use"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid parameter"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    INTERNAL_ERROR("/internal-server-error", "Internal Server Error");

    private String uri;
    private String title;

    ErrorType(String path, String title) {
        this.uri = "https://foodnow.com.br" + path;
        this.title = title;
    }


}
