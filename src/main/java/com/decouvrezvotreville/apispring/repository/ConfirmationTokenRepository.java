package com.decouvrezvotreville.apispring.repository;

import com.decouvrezvotreville.apispring.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.expiresAt = ?2 " +
            "WHERE c.token = ?1")
    int updateExpiredAt(String token, LocalDateTime expiredAt);


    @Transactional
    @Query("select c from ConfirmationToken c where c.user.mail = :email")
    Optional<ConfirmationToken> findByUser(String email);

    @Transactional
    @Modifying
    @Query("delete  from ConfirmationToken c where c.user.mail = :email")
    void deleteByUser(String email);
}