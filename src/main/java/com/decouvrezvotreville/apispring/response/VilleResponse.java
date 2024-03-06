package com.decouvrezvotreville.apispring.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Data
public class VilleResponse {

    private String nom;

    private String codePostale;
}
