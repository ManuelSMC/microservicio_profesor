package com.profesores.asesorias.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "profesor")

@Data
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Integer idProfesor;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "clave")
    private String clave;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profesor_id")
    private List<ProfesorDivision> profesorDivisiones;
}