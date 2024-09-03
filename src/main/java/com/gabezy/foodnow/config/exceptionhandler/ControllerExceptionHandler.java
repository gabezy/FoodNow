package com.gabezy.foodnow.config.exceptionhandler;

import com.gabezy.foodnow.exceptions.BusinessException;
import com.gabezy.foodnow.exceptions.CityNotFoundException;
import com.gabezy.foodnow.exceptions.EntityInUseException;
import com.gabezy.foodnow.exceptions.StateNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.LocalDateTime.now;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StateNotFoundException.class)
    public ResponseEntity<StandardError> handleStateNotFoundException(StateNotFoundException e, HttpServletRequest request) {

       return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildStandardError(e.getMessage(), HttpStatus.NOT_FOUND.value(), request));
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<StandardError> handleCityNotFoundException(CityNotFoundException e, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildStandardError(e.getMessage(), HttpStatus.NOT_FOUND.value(), request));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> handleBusinessException(BusinessException e, HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(buildStandardError(e.getMessage(), HttpStatus.BAD_REQUEST.value(), request));
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<StandardError> handleEntityInUseException(EntityInUseException e, HttpServletRequestWrapper request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).
                body(buildStandardError(e.getMessage(), HttpStatus.CONFLICT.value(), request));
    }



    private StandardError buildStandardError(String message, Integer status, HttpServletRequest request) {
        return StandardError.builder()
                .message(message)
                .status(status)
                .path(request.getRequestURI())
                .timestamp(now())
                .build();
    }

}
