package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.service.VaccinationGroupService;
import com.bzu.smartvax.service.dto.VaccinationGroupDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaccination-groups")
public class VaccinationGroupResource {

    private final VaccinationGroupService service;

    public VaccinationGroupResource(VaccinationGroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VaccinationGroupDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<VaccinationGroupDTO> create(@RequestBody VaccinationGroupDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }
}
