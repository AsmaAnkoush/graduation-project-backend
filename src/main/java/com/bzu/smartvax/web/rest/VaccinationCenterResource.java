package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.service.VaccinationCenterService;
import com.bzu.smartvax.service.dto.VaccinationCenterDTO;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaccination-centers")
public class VaccinationCenterResource {

    private final VaccinationCenterService service;

    public VaccinationCenterResource(VaccinationCenterService service) {
        this.service = service;
    }

    @GetMapping
    public List<VaccinationCenterDTO> getAllCenters() {
        return service.findAll();
    }

    @PostMapping
    public VaccinationCenterDTO createCenter(@RequestBody VaccinationCenterDTO dto) {
        return service.save(dto);
    }
}
