package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.VaccinationService;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VaccinationController {

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationService vaccinationService;

    public VaccinationController(VaccinationRepository vaccinationRepository, VaccinationService vaccinationService) {
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationService = vaccinationService;
    }

    //    @GetMapping("/vaccinations/{id}")
    //    public ResponseEntity<Vaccination> getVaccination(@PathVariable Long id) {
    //        Optional<Vaccination> vaccination = vaccinationRepository.findById(id);
    //        return vaccination.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    //    }

    @GetMapping("/vaccinations/{id}")
    public ResponseEntity<VaccinationDTO> getVaccination(@PathVariable Long id) {
        return vaccinationService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //    @GetMapping("/vaccinations")
    //    public List<Vaccination> getAllVaccinations() {
    //        return vaccinationRepository.findAll();
    //    }

    @GetMapping("/vaccinations")
    public List<VaccinationDTO> getAllVaccinations() {
        return vaccinationService.findAll(); // باستخدام mapper
    }
}
