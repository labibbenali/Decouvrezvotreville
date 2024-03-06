package com.decouvrezvotreville.apispring.repository;
import com.decouvrezvotreville.apispring.entities.ConfirmationToken;
import com.decouvrezvotreville.apispring.entities.MotDePasseOublier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MotDePasseOublierRespository extends JpaRepository<MotDePasseOublier, Long> {

    @Transactional
    @Query("select m from MotDePasseOublier m where m.mail = :email")
    Optional<MotDePasseOublier> findByMail(String email);
}
