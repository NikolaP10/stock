package com.mds.stock.inbound.advices;

import com.mds.stock.inbound.exceptions.EntityAlreadyExistsException;
import com.mds.stock.inbound.exceptions.EntityNotFoundException;
import com.mds.stock.inbound.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse entityNotFoundHandler(EntityNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), "ENTITY_NOT_FOUND");
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse entityAlreadyExistsHandler(EntityAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage(), "ENTITY_ALREADY_EXISTS");
    }
}
