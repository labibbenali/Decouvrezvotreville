package com.decouvrezvotreville.apispring.repository;

import com.decouvrezvotreville.apispring.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VilleRepository extends JpaRepository<Ville, Long> {
}
