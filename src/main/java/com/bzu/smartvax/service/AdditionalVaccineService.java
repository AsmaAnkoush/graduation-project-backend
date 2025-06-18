package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.AdditionalVaccine;
import com.bzu.smartvax.repository.AdditionalVaccineRepository;
import com.bzu.smartvax.service.dto.AdditionalVaccineDTO;
import com.bzu.smartvax.service.mapper.AdditionalVaccineMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AdditionalVaccineService {

    private final AdditionalVaccineRepository repository;
    private final AdditionalVaccineMapper mapper;

    public AdditionalVaccineService(AdditionalVaccineRepository repository, AdditionalVaccineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AdditionalVaccineDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    public Optional<AdditionalVaccineDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public AdditionalVaccineDTO create(AdditionalVaccineDTO dto) {
        AdditionalVaccine saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    public Optional<AdditionalVaccineDTO> update(Long id, AdditionalVaccineDTO dto) {
        return repository
            .findById(id)
            .map(existing -> {
                existing.setName(dto.getName());
                existing.setMinAgeMonths(dto.getMinAgeMonths());
                existing.setDoseCount(dto.getDoseCount());
                existing.setNotes(dto.getNotes());
                return mapper.toDto(repository.save(existing));
            });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
