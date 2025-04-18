package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.repository.ScheduleVaccinationRepository;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.service.mapper.ScheduleVaccinationMapper;
import java.util.Optional;
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
        ScheduleVaccination scheduleVaccination = scheduleVaccinationMapper.toEntity(scheduleVaccinationDTO);
        scheduleVaccination = scheduleVaccinationRepository.save(scheduleVaccination);
        return scheduleVaccinationMapper.toDto(scheduleVaccination);
    }

    /**
     * Update a scheduleVaccination.
     *
     * @param scheduleVaccinationDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleVaccinationDTO update(ScheduleVaccinationDTO scheduleVaccinationDTO) {
        LOG.debug("Request to update ScheduleVaccination : {}", scheduleVaccinationDTO);
        ScheduleVaccination scheduleVaccination = scheduleVaccinationMapper.toEntity(scheduleVaccinationDTO);
        scheduleVaccination = scheduleVaccinationRepository.save(scheduleVaccination);
        return scheduleVaccinationMapper.toDto(scheduleVaccination);
    }

    /**
     * Partially update a scheduleVaccination.
     *
     * @param scheduleVaccinationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ScheduleVaccinationDTO> partialUpdate(ScheduleVaccinationDTO scheduleVaccinationDTO) {
        LOG.debug("Request to partially update ScheduleVaccination : {}", scheduleVaccinationDTO);

        return scheduleVaccinationRepository
            .findById(scheduleVaccinationDTO.getId())
            .map(existingScheduleVaccination -> {
                scheduleVaccinationMapper.partialUpdate(existingScheduleVaccination, scheduleVaccinationDTO);

                return existingScheduleVaccination;
            })
            .map(scheduleVaccinationRepository::save)
            .map(scheduleVaccinationMapper::toDto);
    }

    /**
     * Get all the scheduleVaccinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduleVaccinationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ScheduleVaccinations");
        return scheduleVaccinationRepository.findAll(pageable).map(scheduleVaccinationMapper::toDto);
    }

    /**
     * Get one scheduleVaccination by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScheduleVaccinationDTO> findOne(Long id) {
        LOG.debug("Request to get ScheduleVaccination : {}", id);
        return scheduleVaccinationRepository.findById(id).map(scheduleVaccinationMapper::toDto);
    }

    /**
     * Delete the scheduleVaccination by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ScheduleVaccination : {}", id);
        scheduleVaccinationRepository.deleteById(id);
    }
}
