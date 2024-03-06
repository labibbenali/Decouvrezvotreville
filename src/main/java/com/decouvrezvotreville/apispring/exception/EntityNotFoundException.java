package com.decouvrezvotreville.apispring.exception;

import lombok.Getter;

import java.util.List;

public class EntityNotFoundException  extends RuntimeException{

    @Getter
    private ErrorCodes errorCode;

    @Getter
    private List<String> errors;

    public EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public EntityNotFoundException(String message, Throwable cause, ErrorCodes errorCode){
        super(message, cause);
        this.errorCode=errorCode;
    }

    public EntityNotFoundException(String message, ErrorCodes errorCode){
        super(message);
        this.errorCode=errorCode;
    }
    public EntityNotFoundException(String message,  ErrorCodes errorCode, List<String> errors){
        super(message);
        this.errorCode=errorCode;
        this.errors=errors;
    }
}
