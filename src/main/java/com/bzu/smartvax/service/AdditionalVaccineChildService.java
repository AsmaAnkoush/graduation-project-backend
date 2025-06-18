package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.AdditionalVaccineChild;
import com.bzu.smartvax.repository.AdditionalVaccineChildRepository;
import com.bzu.smartvax.service.dto.AdditionalVaccineChildDTO;
import com.bzu.smartvax.service.mapper.AdditionalVaccineChildMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdditionalVaccineChildService {

    private final AdditionalVaccineChildRepository repository;
    private final AdditionalVaccineChildMapper mapper;

    public AdditionalVaccineChildService(AdditionalVaccineChildRepository repository, AdditionalVaccineChildMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public AdditionalVaccineChildDTO save(AdditionalVaccineChildDTO dto) {
        AdditionalVaccineChild entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public List<AdditionalVaccineChildDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public Optional<AdditionalVaccineChildDTO> findOne(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<AdditionalVaccineChildDTO> findByChildId(String childId) {
        return repository.findByChildId(childId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Optional<AdditionalVaccineChildDTO> updateStatus(Long id, String status) {
        return repository
            .findById(id)
            .map(entity -> {
                entity.setStatus(status);
                if ("COMPLETED".equalsIgnoreCase(status)) {
                    entity.setDateOfAdministration(LocalDate.now());
                }
                return entity;
            })
            .map(repository::save)
            .map(mapper::toDto);
    }

    public List<AdditionalVaccineChildDTO> findByChildIdAndStatus(String childId, String status) {
        return repository.findByChildIdAndStatus(childId, status).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
