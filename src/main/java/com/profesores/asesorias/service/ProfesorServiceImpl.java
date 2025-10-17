package com.profesores.asesorias.service;

import com.profesores.asesorias.client.DivisionClient;
import com.profesores.asesorias.dto.DivisionDTO;
import com.profesores.asesorias.dto.ProfesorDTO;
import com.profesores.asesorias.entity.Profesor;
import com.profesores.asesorias.entity.ProfesorDivision;
import com.profesores.asesorias.repository.ProfesorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepo;

    @Autowired
    private DivisionClient divisionClient;

    @Override
    public List<ProfesorDTO> getAll() {
        List<Profesor> profesores = profesorRepo.findAll();
        Set<Integer> allDivisionIds = profesores.stream()
                .flatMap(p -> p.getProfesorDivisiones().stream().map(ProfesorDivision::getDivisionID))
                .collect(Collectors.toSet());
        List<DivisionDTO> divisionDtos = divisionClient.getDivisionesByIds(new ArrayList<>(allDivisionIds));
        Map<Integer, DivisionDTO> divisionMap = divisionDtos.stream()
                .collect(Collectors.toMap(DivisionDTO::getIdDivision, d -> d));

        return profesores.stream().map(p -> toDto(p, divisionMap)).collect(Collectors.toList());
    }

    @Override
    public ProfesorDTO getById(Integer id) {
        Profesor profesor = profesorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + id));
        Set<Integer> divisionIds = profesor.getProfesorDivisiones().stream()
                .map(ProfesorDivision::getDivisionID).collect(Collectors.toSet());
        List<DivisionDTO> divisionDtos = divisionClient.getDivisionesByIds(new ArrayList<>(divisionIds));
        Map<Integer, DivisionDTO> divisionMap = divisionDtos.stream()
                .collect(Collectors.toMap(DivisionDTO::getIdDivision, d -> d));
        return toDto(profesor, divisionMap);
    }

    @Override
    public ProfesorDTO create(ProfesorDTO dto) {
        Profesor profesor = new Profesor();
        profesor.setNombre(dto.getNombre());
        profesor.setClave(dto.getClave());
        profesor.setDescripcion(dto.getDescripcion());
        profesor.setStatus(dto.getStatus() != null ? dto.getStatus() : true);
        profesor.setProfesorDivisiones(new ArrayList<>());

        if (dto.getDivisionIds() != null) {
            for (Integer divId : dto.getDivisionIds()) {
                // Validar que la división exista
                divisionClient.getDivisionDtoById(divId);
                ProfesorDivision pd = new ProfesorDivision();
                pd.setDivisionID(divId);
                profesor.getProfesorDivisiones().add(pd);
            }
        }

        Profesor saved = profesorRepo.save(profesor);
        return getById(saved.getIdProfesor()); // Obtiene con info de divisiones
    }

    @Override
    public ProfesorDTO update(Integer id, ProfesorDTO dto) {
        Profesor profesor = profesorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + id));

        if (dto.getNombre() != null) {
            profesor.setNombre(dto.getNombre());
        }
        if (dto.getClave() != null) {
            profesor.setClave(dto.getClave());
        }
        if (dto.getDescripcion() != null) {
            profesor.setDescripcion(dto.getDescripcion());
        }
        if (dto.getStatus() != null) {
            profesor.setStatus(dto.getStatus());
        }
        if (dto.getDivisionIds() != null) {
            // Reemplazar todas las divisiones
            profesor.getProfesorDivisiones().clear();
            for (Integer divId : dto.getDivisionIds()) {
                // Validar que la división exista
                divisionClient.getDivisionDtoById(divId);
                ProfesorDivision pd = new ProfesorDivision();
                pd.setDivisionID(divId);
                profesor.getProfesorDivisiones().add(pd);
            }
        }

        Profesor updated = profesorRepo.save(profesor);
        return getById(updated.getIdProfesor());
    }

    @Override
    public String delete(Integer id) {
        Optional<Profesor> profesorOpt = profesorRepo.findById(id);
        if (profesorOpt.isEmpty()) {
            throw new RuntimeException("Profesor no encontrado con id: " + id);
        }
        // Aquí podrías agregar chequeo de uso en programas si se implementa un client similar
        profesorRepo.deleteById(id);
        return "Profesor eliminado exitosamente.";
    }

    @Override
    public void assignDivisionToProfesor(Integer profesorId, Integer divisionId) {
        Profesor profesor = profesorRepo.findById(profesorId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + profesorId));
        // Validar división
        divisionClient.getDivisionDtoById(divisionId);
        // Verificar si ya existe
        boolean exists = profesor.getProfesorDivisiones().stream()
                .anyMatch(pd -> pd.getDivisionID().equals(divisionId));
        if (!exists) {
            ProfesorDivision pd = new ProfesorDivision();
            pd.setDivisionID(divisionId);
            profesor.getProfesorDivisiones().add(pd);
            profesorRepo.save(profesor);
        }
    }

    @Override
    public void removeDivisionFromProfesor(Integer profesorId, Integer divisionId) {
        Profesor profesor = profesorRepo.findById(profesorId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + profesorId));
        profesor.getProfesorDivisiones().removeIf(pd -> pd.getDivisionID().equals(divisionId));
        profesorRepo.save(profesor);
    }

    @Override
    public ProfesorDTO habilitarProfesor(Integer id) {
        Profesor profesor = profesorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + id));
        profesor.setStatus(true);
        Profesor updated = profesorRepo.save(profesor);
        return getById(updated.getIdProfesor());
    }

    @Override
    public ProfesorDTO deshabilitarProfesor(Integer id) {
        Profesor profesor = profesorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + id));
        profesor.setStatus(false);
        Profesor updated = profesorRepo.save(profesor);
        return getById(updated.getIdProfesor());
    }

    private ProfesorDTO toDto(Profesor profesor, Map<Integer, DivisionDTO> divisionMap) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setIdProfesor(profesor.getIdProfesor());
        dto.setNombre(profesor.getNombre());
        dto.setClave(profesor.getClave());
        dto.setDescripcion(profesor.getDescripcion());
        dto.setStatus(profesor.getStatus());
        dto.setDivisionIds(profesor.getProfesorDivisiones().stream().map(ProfesorDivision::getDivisionID).toList());
        dto.setDivisions(profesor.getProfesorDivisiones().stream()
                .map(pd -> divisionMap.get(pd.getDivisionID()))
                .filter(d -> d != null)
                .toList());
        return dto;
    }
}