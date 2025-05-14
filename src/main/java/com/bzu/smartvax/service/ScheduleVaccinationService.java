package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.repository.ScheduleVaccinationRepository;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.service.mapper.ScheduleVaccinationMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bzu.smartvax.domain.ScheduleVaccination}.
 */
@Service
@Transactional
public class ScheduleVaccinationService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleVaccinationService.class);

    private final ScheduleVaccinationRepository scheduleVaccinationRepository;
    private final ScheduleVaccinationMapper scheduleVaccinationMapper;

    public ScheduleVaccinationService(
        ScheduleVaccinationRepository scheduleVaccinationRepository,
        ScheduleVaccinationMapper scheduleVaccinationMapper
    ) {
        this.scheduleVaccinationRepository = scheduleVaccinationRepository;
        this.scheduleVaccinationMapper = scheduleVaccinationMapper;
    }

    /**
     * Save a scheduleVaccination.
     *
     * @param scheduleVaccinationDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleVaccinationDTO save(ScheduleVaccinationDTO scheduleVaccinationDTO) {
        LOG.debug("Request to save ScheduleVaccination : {}", scheduleVaccinationDTO);
        ScheduleVaccination entity = scheduleVaccinationMapper.toEntity(scheduleVaccinationDTO);
        entity = scheduleVaccinationRepository.save(entity);
        return scheduleVaccinationMapper.toDto(entity);
    }

    /**
     * Update a scheduleVaccination.
     *
     * @param scheduleVaccinationDTO the entity to update.
     * @return the updated entity.
     */
    public ScheduleVaccinationDTO update(ScheduleVaccinationDTO scheduleVaccinationDTO) {
        LOG.debug("Request to update ScheduleVaccination : {}", scheduleVaccinationDTO);
        ScheduleVaccination entity = scheduleVaccinationMapper.toEntity(scheduleVaccinationDTO);
        entity = scheduleVaccinationRepository.save(entity);
        return scheduleVaccinationMapper.toDto(entity);
    }

    /**
     * Partially update a scheduleVaccination.
     *
     * @param scheduleVaccinationDTO the entity to update partially.
     * @return the updated entity.
     */
    public Optional<ScheduleVaccinationDTO> partialUpdate(ScheduleVaccinationDTO scheduleVaccinationDTO) {
        LOG.debug("Request to partially update ScheduleVaccination : {}", scheduleVaccinationDTO);

        return scheduleVaccinationRepository
            .findById(scheduleVaccinationDTO.getId())
            .map(existing -> {
                scheduleVaccinationMapper.partialUpdate(existing, scheduleVaccinationDTO);
                return existing;
            })
            .map(scheduleVaccinationRepository::save)
            .map(scheduleVaccinationMapper::toDto);
    }

    /**
     * Get all the scheduleVaccinations with pagination.
     *
     * @param pageable pagination info.
     * @return paged list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduleVaccinationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ScheduleVaccinations (paged)");
        return scheduleVaccinationRepository.findAll(pageable).map(scheduleVaccinationMapper::toDto);
    }

    /**
     * Get all scheduleVaccinations without pagination.
     *
     * @return list of all entities.
     */

    @Transactional(readOnly = true)
    public List<ScheduleVaccinationDTO> findAll() {
        return scheduleVaccinationRepository.findAll().stream().map(scheduleVaccinationMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one scheduleVaccination by ID.
     *
     * @param id the ID.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScheduleVaccinationDTO> findOne(Long id) {
        LOG.debug("Request to get ScheduleVaccination : {}", id);
        return scheduleVaccinationRepository.findById(id).map(scheduleVaccinationMapper::toDto);
    }

    /**
     * Delete scheduleVaccination by ID.
     *
     * @param id the ID.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ScheduleVaccination : {}", id);
        scheduleVaccinationRepository.deleteById(id);
    }
}
