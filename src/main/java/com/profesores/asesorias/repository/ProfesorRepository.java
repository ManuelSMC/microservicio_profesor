package com.profesores.asesorias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profesores.asesorias.entity.Profesor;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    
}