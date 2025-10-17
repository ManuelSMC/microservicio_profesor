package com.profesores.asesorias.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profesorDivision")
@Data
public class ProfesorDivision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "division_id")
    private Integer divisionID;
}