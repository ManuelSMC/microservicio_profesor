package com.profesores.asesorias.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.profesores.asesorias.entity.Profesor;
import com.profesores.asesorias.entity.Division;
import com.profesores.asesorias.dto.ProfesorDTO;
import com.profesores.asesorias.repository.ProfesorRepository;
import com.profesores.asesorias.repository.DivisionRepository;
import com.profesores.asesorias.service.ProfesorService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private DivisionRepository divisionRepository;

    // Crear nuevo profesor
    @PostMapping
    public ResponseEntity<?> crearProfesor(@RequestBody ProfesorDTO profesorDTO) {
        try {
            Profesor profesor = new Profesor();
            profesor.setNombre(profesorDTO.getNombre());
            profesor.setClave(profesorDTO.getClave());
            profesor.setDescripcion(profesorDTO.getDescripcion());
            profesor.setStatus(profesorDTO.getStatus());
            if (profesorDTO.getDivisionId() != null) {
                Division division = divisionRepository.findById(profesorDTO.getDivisionId())
                    .orElseThrow(() -> new RuntimeException("División no encontrada con id: " + profesorDTO.getDivisionId()));
                profesor.setDivision(division);
            }
            profesor.setIdProfesor(null);
            Profesor savedProfesor = profesorService.crearProfesor(profesor);
            return ResponseEntity.ok(savedProfesor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al crear el profesor: " + e.getMessage());
        }
    }

    // Listar todos los profesores
    @GetMapping
    public ResponseEntity<List<Profesor>> getAll() {
        List<Profesor> profesores = profesorRepository.findAll();
        return ResponseEntity.ok(profesores);
    }

    // Buscar profesor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getById(@PathVariable Integer id) {
        Optional<Profesor> profesor = profesorRepository.findById(id);
        if (profesor.isPresent()) {
            return ResponseEntity.ok(profesor.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Editar un profesor
    @PutMapping("/{id_profesor}")
    public ResponseEntity<?> editar(@PathVariable Integer id_profesor, @RequestBody ProfesorDTO profesorDTO) {
        try {
            Optional<Profesor> profesorOpt = profesorRepository.findById(id_profesor);
            if (profesorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profesor no encontrado con ID: " + id_profesor);
            }
            Profesor profesorExistente = profesorOpt.get();
            // Actualizar solo los campos que vienen en el request (no nulos)
            if (profesorDTO.getNombre() != null) {
                profesorExistente.setNombre(profesorDTO.getNombre());
            }
            if (profesorDTO.getClave() != null) {
                profesorExistente.setClave(profesorDTO.getClave());
            }
            if (profesorDTO.getDescripcion() != null) {
                profesorExistente.setDescripcion(profesorDTO.getDescripcion());
            }
            if (profesorDTO.getDivisionId() != null) {
                Division division = divisionRepository.findById(profesorDTO.getDivisionId())
                    .orElseThrow(() -> new RuntimeException("División no encontrada con id: " + profesorDTO.getDivisionId()));
                profesorExistente.setDivision(division);
            }
            // Para el campo booleano status, no verificamos null, ya que siempre tiene un valor
            profesorExistente.setStatus(profesorDTO.getStatus());

            Profesor profesorActualizado = profesorRepository.save(profesorExistente);
            return ResponseEntity.ok(profesorActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el profesor: " + e.getMessage());
        }
    }

    // PUT habilitar
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<Profesor> habilitarProfesor(@PathVariable Integer id) {
        try {
            Profesor updated = profesorService.habilitarProfesor(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT deshabilitar
    @PutMapping("/{id}/deshabilitar")
    public ResponseEntity<Profesor> deshabilitarProfesor(@PathVariable Integer id) {
        try {
            Profesor updated = profesorService.deshabilitarProfesor(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE 
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProfesor(@PathVariable Integer id) {
        try {
            String message = profesorService.deleteProfesor(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}