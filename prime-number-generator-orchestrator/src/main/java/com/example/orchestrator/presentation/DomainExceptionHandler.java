package com.example.orchestrator.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.orchestrator.domain.exceptions.InvalidInputException;
import com.example.orchestrator.domain.exceptions.NotFoundException;

@RestControllerAdvice
public class DomainExceptionHandler {
    @ExceptionHandler(InvalidInputException.class)
    ProblemDetail handleInvalidInput(InvalidInputException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ProblemDetail handleNotFound(NotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
