package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.VaccinationCenter;
import com.bzu.smartvax.repository.VaccinationCenterRepository;
import com.bzu.smartvax.service.dto.VaccinationCenterDTO;
import com.bzu.smartvax.service.mapper.VaccinationCenterMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class VaccinationCenterService {

    private final VaccinationCenterRepository repository;
    private final VaccinationCenterMapper mapper;

    public VaccinationCenterService(VaccinationCenterRepository repository, VaccinationCenterMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<VaccinationCenterDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public VaccinationCenterDTO save(VaccinationCenterDTO dto) {
        VaccinationCenter entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }
}
