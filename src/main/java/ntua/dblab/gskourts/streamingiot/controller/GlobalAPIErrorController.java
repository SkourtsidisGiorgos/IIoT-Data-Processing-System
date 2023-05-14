package ntua.dblab.gskourts.streamingiot.controller;

import ntua.dblab.gskourts.streamingiot.model.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
public class GlobalAPIErrorController {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected GenericResponse<String, String> handleConflict(
            RuntimeException ex, WebRequest request) {
        return GenericResponse.factoryError(ex.getMessage(), HttpStatus.CONFLICT.toString());
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    protected GenericResponse<String, String> handleHttpMessageNotReadableException(
            RuntimeException ex, WebRequest request) {
        return GenericResponse.factoryError(ex.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }
}