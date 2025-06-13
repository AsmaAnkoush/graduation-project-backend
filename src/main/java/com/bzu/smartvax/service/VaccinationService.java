package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import com.bzu.smartvax.service.mapper.VaccinationMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationMapper vaccinationMapper;

    public VaccinationService(VaccinationRepository vaccinationRepository, VaccinationMapper vaccinationMapper) {
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationMapper = vaccinationMapper;
    }

    //    public Optional<VaccinationDTO> findById(Long id) {
    //        return vaccinationRepository.findById(id).map(vaccinationMapper::toDto);
    //    }

    public Optional<VaccinationDTO> findById(Long id) {
        return vaccinationRepository.findByIdWithGroupAndVaccineType(id).map(vaccinationMapper::toDto);
    }

    public List<VaccinationDTO> findAll() {
        return vaccinationRepository.findAll().stream().map(vaccinationMapper::toDto).collect(Collectors.toList());
    }
}
