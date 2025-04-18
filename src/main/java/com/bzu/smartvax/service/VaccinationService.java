package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import com.bzu.smartvax.service.mapper.VaccinationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bzu.smartvax.domain.Vaccination}.
 */
@Service
@Transactional
public class VaccinationService {

    private static final Logger LOG = LoggerFactory.getLogger(VaccinationService.class);

    private final VaccinationRepository vaccinationRepository;

    private final VaccinationMapper vaccinationMapper;

    public VaccinationService(VaccinationRepository vaccinationRepository, VaccinationMapper vaccinationMapper) {
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationMapper = vaccinationMapper;
    }

    /**
     * Save a vaccination.
     *
     * @param vaccinationDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccinationDTO save(VaccinationDTO vaccinationDTO) {
        LOG.debug("Request to save Vaccination : {}", vaccinationDTO);
        Vaccination vaccination = vaccinationMapper.toEntity(vaccinationDTO);
        vaccination = vaccinationRepository.save(vaccination);
        return vaccinationMapper.toDto(vaccination);
    }

    /**
     * Update a vaccination.
     *
     * @param vaccinationDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccinationDTO update(VaccinationDTO vaccinationDTO) {
        LOG.debug("Request to update Vaccination : {}", vaccinationDTO);
        Vaccination vaccination = vaccinationMapper.toEntity(vaccinationDTO);
        vaccination = vaccinationRepository.save(vaccination);
        return vaccinationMapper.toDto(vaccination);
    }

    /**
     * Partially update a vaccination.
     *
     * @param vaccinationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VaccinationDTO> partialUpdate(VaccinationDTO vaccinationDTO) {
        LOG.debug("Request to partially update Vaccination : {}", vaccinationDTO);

        return vaccinationRepository
            .findById(vaccinationDTO.getId())
            .map(existingVaccination -> {
                vaccinationMapper.partialUpdate(existingVaccination, vaccinationDTO);

                return existingVaccination;
            })
            .map(vaccinationRepository::save)
            .map(vaccinationMapper::toDto);
    }

    /**
     * Get all the vaccinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VaccinationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vaccinations");
        return vaccinationRepository.findAll(pageable).map(vaccinationMapper::toDto);
    }

    /**
     * Get one vaccination by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VaccinationDTO> findOne(Long id) {
        LOG.debug("Request to get Vaccination : {}", id);
        return vaccinationRepository.findById(id).map(vaccinationMapper::toDto);
    }

    /**
     * Delete the vaccination by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vaccination : {}", id);
        vaccinationRepository.deleteById(id);
    }
}
