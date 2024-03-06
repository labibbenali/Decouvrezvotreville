package com.decouvrezvotreville.apispring.handlers;

import com.decouvrezvotreville.apispring.exception.EntityNotFoundException;
import com.decouvrezvotreville.apispring.exception.InvalidEntityException;

import com.decouvrezvotreville.apispring.exception.VerificationEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

//The @RestControllerAdvice annotation is specialization of
// @Component annotation so that it is auto-detected via classpath scanning.
// It is a kind of interceptor that surrounds the logic in our Controllers and
// allows us to apply some common logic to them.
//Rest Controller Advice’s methods (annotated with @ExceptionHandler) are
// shared globally across multiple @Controller components to capture exceptions and
// translate them to HTTP responses.
// The @ExceptionHandler annotation indicates which type of Exception we want to handle.
//The exception instance and the request will be injected via method arguments.

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ErrorDto>
    handleException(InvalidEntityException exception , WebRequest webRequest){
logger.error(exception);
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
               final ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return new ResponseEntity<>(errorDto,badRequest);

    }
    @ExceptionHandler(VerificationEmailException.class)
    public ResponseEntity<ErrorDto>
    handleException(VerificationEmailException exception , WebRequest webRequest){
        logger.error(exception);
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
               final ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return new ResponseEntity<>(errorDto,badRequest);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto>
    handleException(EntityNotFoundException exception , WebRequest webRequest){
        logger.error(exception);
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return new ResponseEntity<>(errorDto,badRequest);

    }



}
