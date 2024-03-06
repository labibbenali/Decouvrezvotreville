package com.decouvrezvotreville.apispring.response;

import com.decouvrezvotreville.apispring.entities.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String mail;
    private String nom;
    private String prenom;

    private String adresse;

    private LocalDate dateNaissance;
    private String numTel;

    private VilleResponse ville;

    private boolean isActif;

    private boolean isDesactiverByAdmin;

    private Role role;

    private List<PointInteretResponse> pointInteret ;

}
