package com.decouvrezvotreville.apispring.exception;

public enum ErrorCodes {

    USER_NOT_FOUND(1000),
    POINTINTERET_NOT_FOUND(1000),
    USER_NOT_VALID(1001),
    VILLE_NOT_VALID(1002),
    EMAIL_ALREADY_TAKEN(1003),
    EMAIL_INVALID(1004),
    ACCOUNT_NOT_ACTIVATED(1005),
    ERREUR_VERIFICATION_EMAIL(1006),
    CODE_NOT_VALID(1007),
    ACCOUNT_IS_DESACTIVATED(1008),
    ACTION_NOT_AUTHORIZED(1009);


    private int code;

    ErrorCodes(int code){
        this.code=code;
    }

    public  int getCodes(){
        return code;
    }

}
