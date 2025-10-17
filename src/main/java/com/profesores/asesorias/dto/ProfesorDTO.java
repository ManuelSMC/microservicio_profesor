package com.profesores.asesorias.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProfesorDTO {

    private Integer idProfesor;
    private String nombre;
    private String clave;
    private String descripcion;
    private Boolean status;
    private List<Integer> divisionIds; // Para entrada en create/update
    private List<DivisionDto> divisions; // Para salida con info de divisiones
}