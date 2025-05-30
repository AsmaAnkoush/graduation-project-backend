package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import com.bzu.smartvax.service.mapper.VaccinationMapper;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationMapper vaccinationMapper;

    public VaccinationService(VaccinationRepository vaccinationRepository, VaccinationMapper vaccinationMapper) {
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationMapper = vaccinationMapper;
    }

    public Optional<VaccinationDTO> findById(Long id) {
        return vaccinationRepository.findById(id).map(vaccinationMapper::toDto);
    }
}
