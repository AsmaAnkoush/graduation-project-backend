package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.VaccinationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VaccinationController {

    private final VaccinationRepository vaccinationRepository;

    public VaccinationController(VaccinationRepository vaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
    }

    @GetMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccination> getVaccination(@PathVariable Long id) {
        Optional<Vaccination> vaccination = vaccinationRepository.findById(id);
        return vaccination.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/vaccinations")
    public List<Vaccination> getAllVaccinations() {
        return vaccinationRepository.findAll();
    }
}
