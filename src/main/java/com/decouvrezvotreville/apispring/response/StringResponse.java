package com.decouvrezvotreville.apispring.response;

import lombok.Data;

@Data
public class StringResponse {
    private String response;

    public StringResponse(String response) {
        this.response = response;
    }
}
