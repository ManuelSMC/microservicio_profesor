package com.profesores.asesorias.controller;

import com.profesores.asesorias.dto.ProfesorDTO;
import com.profesores.asesorias.service.ProfesorService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    // Crear nuevo profesor
    @PostMapping
    public ResponseEntity<?> crearProfesor(@RequestBody ProfesorDTO profesorDTO) {
        try {
            ProfesorDTO savedDto = profesorService.create(profesorDTO);
            return ResponseEntity.ok(savedDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear el profesor: " + e.getMessage());
        }
    }

    // Listar todos los profesores
    @GetMapping
    public ResponseEntity<List<ProfesorDTO>> getAll() {
        List<ProfesorDTO> profesores = profesorService.getAll();
        return ResponseEntity.ok(profesores);
    }

    // Buscar profesor por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDTO> getById(@PathVariable Integer id) {
        try {
            ProfesorDTO dto = profesorService.getById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Editar un profesor
    @PutMapping("/{id_profesor}")
    public ResponseEntity<?> editar(@PathVariable Integer id_profesor, @RequestBody ProfesorDTO profesorDTO) {
        try {
            ProfesorDTO updatedDto = profesorService.update(id_profesor, profesorDTO);
            return ResponseEntity.ok(updatedDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profesor no encontrado con ID: " + id_profesor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el profesor: " + e.getMessage());
        }
    }

    // Habilitar profesor
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<ProfesorDTO> habilitarProfesor(@PathVariable Integer id) {
        try {
            ProfesorDTO updated = profesorService.habilitarProfesor(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Deshabilitar profesor
    @PutMapping("/{id}/deshabilitar")
    public ResponseEntity<ProfesorDTO> deshabilitarProfesor(@PathVariable Integer id) {
        try {
            ProfesorDTO updated = profesorService.deshabilitarProfesor(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Asignar división a profesor
    @PostMapping("/{id}/assign-division/{divisionId}")
    public ResponseEntity<Void> assignDivision(@PathVariable Integer id, @PathVariable Integer divisionId) {
        try {
            profesorService.assignDivisionToProfesor(id, divisionId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Remover división de profesor
    @PostMapping("/{id}/remove-division/{divisionId}")
    public ResponseEntity<Void> removeDivision(@PathVariable Integer id, @PathVariable Integer divisionId) {
        try {
            profesorService.removeDivisionFromProfesor(id, divisionId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Borrar profesor
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProfesor(@PathVariable Integer id) {
        try {
            String message = profesorService.delete(id);
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