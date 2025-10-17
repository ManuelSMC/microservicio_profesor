package com.profesores.asesorias.service;

import com.profesores.asesorias.dto.ProfesorDTO;
import java.util.List;

public interface ProfesorService {

    List<ProfesorDTO> getAll();
    ProfesorDTO getById(Integer id);
    ProfesorDTO create(ProfesorDTO dto);
    ProfesorDTO update(Integer id, ProfesorDTO dto);
    String delete(Integer id);
    void assignDivisionToProfesor(Integer profesorId, Integer divisionId);
    void removeDivisionFromProfesor(Integer profesorId, Integer divisionId);
    ProfesorDTO habilitarProfesor(Integer id);
    ProfesorDTO deshabilitarProfesor(Integer id);
}