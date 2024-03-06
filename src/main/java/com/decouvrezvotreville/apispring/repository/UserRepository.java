package com.decouvrezvotreville.apispring.repository;

import com.decouvrezvotreville.apispring.entities.PointInteret;
import com.decouvrezvotreville.apispring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
@Transactional
public interface UserRepository extends JpaRepository<User, String> {


    void deleteUserByMail(String email);


    Optional<User> findByMail(String mail);

    @Transactional
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE :pointInteret MEMBER OF u.pointInteret")
    boolean existsByPointInteret(@Param("pointInteret") PointInteret pointInteret);


    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.actif = TRUE WHERE a.mail = ?1")
    int enableUser(String email);
}
