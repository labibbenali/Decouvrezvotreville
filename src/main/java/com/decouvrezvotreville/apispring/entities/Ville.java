package com.decouvrezvotreville.apispring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ville", schema = "decouvrezvotreville")
public class Ville implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY

    )
    private Long id;

    @Column(name = "code_insee", nullable = false, length = 250)
    private String codeInsee;

    @Column(name = "nom", nullable = false, length = 250)
    private String nom;

    @Column(name = "date_creation", nullable = false, columnDefinition = "datetime not null")
    private LocalDateTime dateCreation;

    @Column(name = "dernier_modification", nullable = true)
    private LocalDateTime dernierModification;
    @Column(name = "code_postale", nullable = false, length = 10)
    private String codePostale;


    @JsonIgnore
    @OneToMany(mappedBy = "ville")
    private List<User> user;

}