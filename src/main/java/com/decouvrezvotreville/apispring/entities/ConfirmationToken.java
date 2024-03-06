package com.decouvrezvotreville.apispring.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken implements Serializable {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY

    )
    private Long id;
    @Column(nullable = false)
    private String token;


    @Column(nullable = false, columnDefinition = "datetime not null")
    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "datetime not null")
    private LocalDateTime expiresAt;
    @Column(nullable = true, columnDefinition = "datetime not null")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable= false,
            name = "user_id"
    )
    private User user;

    public ConfirmationToken(String token,LocalDateTime createdAt,
                             LocalDateTime expiresAt ,User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user=user;
    }
}
