package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.HealthWorker;
import com.bzu.smartvax.repository.HealthWorkerRepository;
import com.bzu.smartvax.service.dto.HealthWorkerDTO;
import com.bzu.smartvax.service.mapper.HealthWorkerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bzu.smartvax.domain.HealthWorker}.
 */
@Service
@Transactional
public class HealthWorkerService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthWorkerService.class);

    private final HealthWorkerRepository healthWorkerRepository;

    private final HealthWorkerMapper healthWorkerMapper;

    public HealthWorkerService(HealthWorkerRepository healthWorkerRepository, HealthWorkerMapper healthWorkerMapper) {
        this.healthWorkerRepository = healthWorkerRepository;
        this.healthWorkerMapper = healthWorkerMapper;
    }

    /**
     * Save a healthWorker.
     *
     * @param healthWorkerDTO the entity to save.
     * @return the persisted entity.
     */
    public HealthWorkerDTO save(HealthWorkerDTO healthWorkerDTO) {
        LOG.debug("Request to save HealthWorker : {}", healthWorkerDTO);
        HealthWorker healthWorker = healthWorkerMapper.toEntity(healthWorkerDTO);
        healthWorker = healthWorkerRepository.save(healthWorker);
        return healthWorkerMapper.toDto(healthWorker);
    }

    /**
     * Update a healthWorker.
     *
     * @param healthWorkerDTO the entity to save.
     * @return the persisted entity.
     */
    public HealthWorkerDTO update(HealthWorkerDTO healthWorkerDTO) {
        LOG.debug("Request to update HealthWorker : {}", healthWorkerDTO);
        HealthWorker healthWorker = healthWorkerMapper.toEntity(healthWorkerDTO);
        healthWorker = healthWorkerRepository.save(healthWorker);
        return healthWorkerMapper.toDto(healthWorker);
    }

    /**
     * Partially update a healthWorker.
     *
     * @param healthWorkerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HealthWorkerDTO> partialUpdate(HealthWorkerDTO healthWorkerDTO) {
        LOG.debug("Request to partially update HealthWorker : {}", healthWorkerDTO);

        return healthWorkerRepository
            .findById(healthWorkerDTO.getId())
            .map(existingHealthWorker -> {
                healthWorkerMapper.partialUpdate(existingHealthWorker, healthWorkerDTO);

                return existingHealthWorker;
            })
            .map(healthWorkerRepository::save)
            .map(healthWorkerMapper::toDto);
    }

    /**
     * Get all the healthWorkers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HealthWorkerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HealthWorkers");
        return healthWorkerRepository.findAll(pageable).map(healthWorkerMapper::toDto);
    }

    /**
     * Get one healthWorker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HealthWorkerDTO> findOne(Long id) {
        LOG.debug("Request to get HealthWorker : {}", id);
        return healthWorkerRepository.findById(id).map(healthWorkerMapper::toDto);
    }

    /**
     * Delete the healthWorker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HealthWorker : {}", id);
        healthWorkerRepository.deleteById(id);
    }

    public HealthWorkerDTO mapToDto(HealthWorker hw) {
        return healthWorkerMapper.toDto(hw);
    }
}
