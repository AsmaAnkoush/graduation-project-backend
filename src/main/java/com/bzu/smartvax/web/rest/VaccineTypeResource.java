package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.service.VaccineTypeService;
import com.bzu.smartvax.service.dto.VaccineTypeDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaccine-types")
public class VaccineTypeResource {

    private final VaccineTypeService service;

    public VaccineTypeResource(VaccineTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VaccineTypeDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<VaccineTypeDTO> create(@RequestBody VaccineTypeDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }
}
