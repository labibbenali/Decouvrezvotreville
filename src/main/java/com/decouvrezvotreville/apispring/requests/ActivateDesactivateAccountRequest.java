package com.decouvrezvotreville.apispring.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivateDesactivateAccountRequest {

    private String email;
    private boolean actif;


}
