package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.HealthRecord;
import com.bzu.smartvax.repository.HealthRecordRepository;
import com.bzu.smartvax.service.dto.HealthRecordDTO;
import com.bzu.smartvax.service.mapper.HealthRecordMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bzu.smartvax.domain.HealthRecord}.
 */
@Service
@Transactional
public class HealthRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthRecordService.class);

    private final HealthRecordRepository healthRecordRepository;

    private final HealthRecordMapper healthRecordMapper;

    public HealthRecordService(HealthRecordRepository healthRecordRepository, HealthRecordMapper healthRecordMapper) {
        this.healthRecordRepository = healthRecordRepository;
        this.healthRecordMapper = healthRecordMapper;
    }

    /**
     * Save a healthRecord.
     *
     * @param healthRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public HealthRecordDTO save(HealthRecordDTO healthRecordDTO) {
        LOG.debug("Request to save HealthRecord : {}", healthRecordDTO);
        HealthRecord healthRecord = healthRecordMapper.toEntity(healthRecordDTO);
        healthRecord = healthRecordRepository.save(healthRecord);
        return healthRecordMapper.toDto(healthRecord);
    }

    /**
     * Update a healthRecord.
     *
     * @param healthRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public HealthRecordDTO update(HealthRecordDTO healthRecordDTO) {
        LOG.debug("Request to update HealthRecord : {}", healthRecordDTO);
        HealthRecord healthRecord = healthRecordMapper.toEntity(healthRecordDTO);
        healthRecord = healthRecordRepository.save(healthRecord);
        return healthRecordMapper.toDto(healthRecord);
    }

    /**
     * Partially update a healthRecord.
     *
     * @param healthRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HealthRecordDTO> partialUpdate(HealthRecordDTO healthRecordDTO) {
        LOG.debug("Request to partially update HealthRecord : {}", healthRecordDTO);

        return healthRecordRepository
            .findById(healthRecordDTO.getId())
            .map(existingHealthRecord -> {
                healthRecordMapper.partialUpdate(existingHealthRecord, healthRecordDTO);

                return existingHealthRecord;
            })
            .map(healthRecordRepository::save)
            .map(healthRecordMapper::toDto);
    }

    /**
     * Get all the healthRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HealthRecordDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HealthRecords");
        return healthRecordRepository.findAll(pageable).map(healthRecordMapper::toDto);
    }

    /**
     *  Get all the healthRecords where Child is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HealthRecordDTO> findAllWhereChildIsNull() {
        LOG.debug("Request to get all healthRecords where Child is null");
        return StreamSupport.stream(healthRecordRepository.findAll().spliterator(), false)
            .filter(healthRecord -> healthRecord.getChild() == null)
            .map(healthRecordMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one healthRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HealthRecordDTO> findOne(Long id) {
        LOG.debug("Request to get HealthRecord : {}", id);
        return healthRecordRepository.findById(id).map(healthRecordMapper::toDto);
    }

    /**
     * Delete the healthRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HealthRecord : {}", id);
        healthRecordRepository.deleteById(id);
    }
}
