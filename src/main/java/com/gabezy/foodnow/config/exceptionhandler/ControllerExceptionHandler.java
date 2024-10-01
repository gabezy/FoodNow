package com.gabezy.foodnow.config.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.gabezy.foodnow.exceptions.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private static final String MSG_ERROR_GENERIC_USER = "An unexpected internal server error has occurred. Try again and if the problem persist, " +
            "contact your system administrator";


    @Override // trata o erro na serialização do jackson para objeto java
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException cause) {
            return handleInvalidFormatException(cause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException cause) {
            return handlePropertyBindingException(cause, headers, status, request);
        }

        String message = "Unexpected character at request body. Please verify syntax error";
        return handleExceptionInternal(ex, message, headers, status, request, ErrorType.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException e) {
            // Exception ocorre quando passa um tipo inválido como parâmetro da url
            return handleMethodArgumentTypeMismatchException(e, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var detail = String.format("the resource '%s' is not available", ex.getResourcePath());
        return handleExceptionInternal(ex, detail, headers, status, request, ErrorType.RESOURCE_NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var detail = String.format("The resource %s is not available", ex.getRequestURL());
        return handleExceptionInternal(ex, detail, headers, status, request, ErrorType.RESOURCE_NOT_FOUND);
    }

    // Exceção acionada quando um payload passado na requisição viola uma constraint da bean validation
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var detail = "One or more fields are invalid";
        var fields = new HashMap<String, String>();
        handleObjectsErrorToFields(ex, fields);
        var errorType = ErrorType.INVALID_FIELDS;
        var error = createResponseErrorBuilder(detail, status, errorType)
                .fields(fields)
                .build();
        return handleExceptionInternal(ex, error, headers, status, request, errorType);
    }

    @ExceptionHandler(StateNotFoundException.class)
    public ResponseEntity<Object> handleStateNotFoundException(StateNotFoundException ex, WebRequest request) {
        return this.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request, ErrorType.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<Object> handleCityNotFoundException(CityNotFoundException ex, WebRequest request) {
        return this.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request, ErrorType.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException (EntityNotFoundException ex, WebRequest request) {
        return this.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request, ErrorType.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request, ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<Object> handleEntityInUseException(EntityInUseException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request, ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptionType(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return handleExceptionInternal(ex, MSG_ERROR_GENERIC_USER, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request, ErrorType.INTERNAL_ERROR);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request, ErrorType type) {
        if (body == null) {
            body = createResponseErrorBuilder(null, statusCode, type).build();
        } else if (body instanceof String message) {
            body = createResponseErrorBuilder(message, statusCode, type).build();
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    // Lida com os erros de campos existentes, mas informado com o tipo errado
    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String property = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
        var detail = String.format("the property '%s' receive the value '%s' that is an invalid type. " +
                "Correct and inform a type compatible with %s", property, ex.getValue(), ex.getTargetType().getSimpleName());
        var errorType = ErrorType.BAD_REQUEST;
        var error = createResponseErrorBuilder(detail, statusCode, errorType)
                .userMessage(MSG_ERROR_GENERIC_USER)
                .build();
        return handleExceptionInternal(ex, error, headers, statusCode, request, errorType);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
                                                                  HttpStatusCode statusCode, WebRequest request)
    {
        var fields = new HashMap<String, String >();
        ex.getPath().forEach(cv -> {
            // FIXME: Bug when some unknown property show appears as object at the body (Ex: cuisine {"unknownProperty" : "dsada"}
            if (cv.getFieldName() != null) {
                fields.put(cv.getFieldName(), "Invalid property");
            }
        });
        var errorType = ErrorType.BAD_REQUEST;
        var error = createResponseErrorBuilder("Unexpected property at request body", statusCode, errorType)
                .fields(fields)
                .userMessage(MSG_ERROR_GENERIC_USER)
                .build();
        return handleExceptionInternal(ex, error, headers, statusCode, request, errorType);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                             HttpHeaders headers, HttpStatusCode status,
                                                                             WebRequest request)
    {
        var message = String.format("The URL parameter '%s' receive the value '%s' that is a invalid type. " +
                "Correct and inform a value compatible with %s type", ex.getName(), ex.getValue(),
                ex.getParameter().getParameterType().getSimpleName());
        return handleExceptionInternal(ex, message, headers, status, request, ErrorType.INVALID_PARAMETER);
    }

    private ResponseError.ResponseErrorBuilder createResponseErrorBuilder(@Nullable String detail,
                                                                          HttpStatusCode status, ErrorType type)
    {
        return ResponseError.builder()
                .title(type.getTitle())
                .status(status.value())
                .detail(detail)
                .type(type.getUri())
                .timestamp(LocalDateTime.now());
    }

    private void handleObjectsErrorToFields(MethodArgumentNotValidException e,Map<String, String> fields) {
        e.getBindingResult().getAllErrors()
                .forEach(objectError -> {
                    String message = Optional.of(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                            .orElse(objectError.getDefaultMessage());
                    String name = objectError.getObjectName() + " (Entity)";

                    if (objectError instanceof FieldError fieldError) {
                        name = fieldError.getField();
                    }
                    fields.put(name, message);
                });
    }

}
