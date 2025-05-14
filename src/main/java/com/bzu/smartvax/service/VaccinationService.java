package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.VaccinationRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;

    public VaccinationService(VaccinationRepository vaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
    }

    public Optional<Vaccination> findById(Long id) {
        return vaccinationRepository.findById(id);
    }
}
