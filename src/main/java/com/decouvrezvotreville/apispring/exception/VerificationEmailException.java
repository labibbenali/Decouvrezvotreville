package com.decouvrezvotreville.apispring.exception;

import lombok.Getter;

import java.util.List;

public class VerificationEmailException extends RuntimeException {
    @Getter
    private ErrorCodes errorCode;
    @Getter
    private List<String> errors;

    public VerificationEmailException(String message){
        super(message);
    }

    public VerificationEmailException(String message, Throwable cause){
        super(message, cause);
    }

    public VerificationEmailException(String message, Throwable cause, ErrorCodes errorCode){
        super(message, cause);
        this.errorCode=errorCode;
    }

    public VerificationEmailException(String message,  ErrorCodes errorCode, List<String> errors){
        super(message);
        this.errorCode=errorCode;
        this.errors=errors;
    }
    public VerificationEmailException(String message, ErrorCodes errorCode){
        super(message);
        this.errorCode=errorCode;
    }

}
