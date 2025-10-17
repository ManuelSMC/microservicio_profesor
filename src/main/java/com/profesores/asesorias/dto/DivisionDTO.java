package com.profesores.asesorias.dto;

import lombok.Data;

@Data
public class DivisionDTO {

    private Integer idDivision;
    private String nombre;
    private Boolean status;
    private String clave;
    private String descripcion;
    private String  director;
}