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
@Table(name = "point_interet", schema = "decouvrezvotreville")
public class PointInteret implements Serializable {


    @Id
    @Column(name = "point_interet", nullable = false, length = 250)
    private String pointInteret;
    @Column(name = "date_creation", nullable = false, columnDefinition = "datetime not null")
    private LocalDateTime dateCreation;

    @Column(name = "dernier_modification", nullable = true,  columnDefinition = "datetime not null")
    private LocalDateTime dernierModification;


    @JsonIgnore
    @ManyToMany(mappedBy = "pointInteret")
    private List<User> user;
}