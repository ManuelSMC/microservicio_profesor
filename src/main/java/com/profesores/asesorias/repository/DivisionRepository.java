package com.profesores.asesorias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profesores.asesorias.entity.Division;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Integer> {
    
}