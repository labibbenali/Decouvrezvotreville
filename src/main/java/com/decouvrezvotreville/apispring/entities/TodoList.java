package com.decouvrezvotreville.apispring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todo_list", schema = "decouvrezvotreville")
public class TodoList implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom_activite", nullable = false, length = 250)
    private String nomActivite;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false, columnDefinition = "datetime not null")
    private LocalDateTime date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mail", nullable = false)
    private User mail;

}