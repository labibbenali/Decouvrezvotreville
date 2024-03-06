package com.decouvrezvotreville.apispring.requests;

import com.decouvrezvotreville.apispring.entities.Role;
import com.decouvrezvotreville.apispring.entities.Ville;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String mail;
    private String motDePasse;
    private String adresse;
    private LocalDate dateNaissance;
    private Ville ville;
    private Role role;

}
