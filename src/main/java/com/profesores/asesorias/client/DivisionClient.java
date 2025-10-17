package com.profesores.asesorias.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.profesores.asesorias.dto.DivisionDTO;

@FeignClient(name = "microservicio-division")
public interface DivisionClient {

    @GetMapping("/api/divisiones/{id}")
    DivisionDTO getDivisionDtoById(@PathVariable Integer id);

    @PostMapping("/api/divisiones/by-ids")
    List<DivisionDTO> getDivisionesByIds(@RequestBody List<Integer> ids);
}