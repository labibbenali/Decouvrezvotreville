package com.decouvrezvotreville.apispring.repository;

import com.decouvrezvotreville.apispring.entities.PointInteret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface PointInteretRepository extends JpaRepository<PointInteret, String> {

}
