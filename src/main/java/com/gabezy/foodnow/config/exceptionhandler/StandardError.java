package com.gabezy.foodnow.config.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StandardError {

    private LocalDateTime timestamp;
    private String message;
    private Integer status;
    private String path;
    private String title;
    private String detail;


}
