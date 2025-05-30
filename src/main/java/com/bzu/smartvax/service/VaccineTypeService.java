package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.VaccineType;
import com.bzu.smartvax.repository.VaccineTypeRepository;
import com.bzu.smartvax.service.dto.VaccineTypeDTO;
import com.bzu.smartvax.service.mapper.VaccineTypeMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class VaccineTypeService {

    private final VaccineTypeRepository repository;
    private final VaccineTypeMapper mapper;

    public VaccineTypeService(VaccineTypeRepository repository, VaccineTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<VaccineTypeDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public VaccineTypeDTO save(VaccineTypeDTO dto) {
        VaccineType entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }
}
