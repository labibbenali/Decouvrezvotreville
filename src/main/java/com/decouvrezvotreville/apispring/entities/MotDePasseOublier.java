package com.decouvrezvotreville.apispring.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "motDepasseOublier", schema = "decouvrezvotreville")
public class MotDePasseOublier{
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY

    )
    private Long id;

    @Column(name = "mail", nullable = false, length = 250)
    private String mail;

    @Column(name = "code", nullable = false)
    private Integer code;

}
