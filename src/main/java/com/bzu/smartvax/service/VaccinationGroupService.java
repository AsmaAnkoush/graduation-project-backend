package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.VaccinationGroup;
import com.bzu.smartvax.repository.VaccinationGroupRepository;
import com.bzu.smartvax.service.dto.VaccinationGroupDTO;
import com.bzu.smartvax.service.mapper.VaccinationGroupMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class VaccinationGroupService {

    private final VaccinationGroupRepository repository;
    private final VaccinationGroupMapper mapper;

    public VaccinationGroupService(VaccinationGroupRepository repository, VaccinationGroupMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<VaccinationGroupDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public VaccinationGroupDTO save(VaccinationGroupDTO dto) {
        VaccinationGroup entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }
}
