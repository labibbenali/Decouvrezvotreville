package com.decouvrezvotreville.apispring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "decouvrezvotreville")
public class User  implements UserDetails, Serializable {
    @Id
    @Column(name = "mail", nullable = false, length = 250)
    private String mail;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "adresse", nullable = false, length = 250)
    private String adresse;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "date_creation", nullable = false, columnDefinition = "datetime not null")
    private LocalDateTime dateCreation;

    @Column(name = "dernier_modification")
    private LocalDateTime dernierModification;

    @Column(name = "actif", nullable = true)
    private Boolean actif = false;

    @Column(name = "num_Tel", nullable = false)
    private int numTel;

    @Column(name = "desactiverByAdmin", nullable = true)
    private Boolean desactiverByAdmin = false;


    @ManyToOne()
    @JoinColumn(name = "id_ville", nullable = false)
    private Ville ville;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "mail")
    private List<TodoList> todoList ;

    @ManyToMany
    @JoinTable(
            name="point_interet_user",
            joinColumns =  @JoinColumn(name="mail"),
            inverseJoinColumns = @JoinColumn(name="point_interet_id")
    )
    private List<PointInteret> pointInteret ;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @JsonIgnore
    @Override
    public String getPassword() {
        return motDePasse;
    }
    @JsonIgnore
    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return actif;
    }
}