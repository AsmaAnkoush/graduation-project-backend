package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.repository.ParentRepository;
import com.bzu.smartvax.service.dto.ParentDTO;
import com.bzu.smartvax.service.mapper.ParentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bzu.smartvax.domain.Parent}.
 */
@Service
@Transactional
public class ParentService {

    private static final Logger LOG = LoggerFactory.getLogger(ParentService.class);

    private final ParentRepository parentRepository;

    private final ParentMapper parentMapper;

    public ParentService(ParentRepository parentRepository, ParentMapper parentMapper) {
        this.parentRepository = parentRepository;
        this.parentMapper = parentMapper;
    }

    /**
     * Save a parent.
     *
     * @param parentDTO the entity to save.
     * @return the persisted entity.
     */
    public ParentDTO save(ParentDTO parentDTO) {
        LOG.debug("Request to save Parent : {}", parentDTO);
        Parent parent = parentMapper.toEntity(parentDTO);
        parent = parentRepository.save(parent);
        return parentMapper.toDto(parent);
    }

    /**
     * Update a parent.
     *
     * @param parentDTO the entity to save.
     * @return the persisted entity.
     */
    public ParentDTO update(ParentDTO parentDTO) {
        LOG.debug("Request to update Parent : {}", parentDTO);
        Parent parent = parentMapper.toEntity(parentDTO);
        parent = parentRepository.save(parent);
        return parentMapper.toDto(parent);
    }

    /**
     * Partially update a parent.
     *
     * @param parentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParentDTO> partialUpdate(ParentDTO parentDTO) {
        LOG.debug("Request to partially update Parent : {}", parentDTO);

        return parentRepository
            .findById(parentDTO.getId())
            .map(existingParent -> {
                parentMapper.partialUpdate(existingParent, parentDTO);

                return existingParent;
            })
            .map(parentRepository::save)
            .map(parentMapper::toDto);
    }

    /**
     * Get all the parents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Parents");
        return parentRepository.findAll(pageable).map(parentMapper::toDto);
    }

    /**
     * Get one parent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParentDTO> findOne(Long id) {
        LOG.debug("Request to get Parent : {}", id);
        return parentRepository.findById(id).map(parentMapper::toDto);
    }

    /**
     * Delete the parent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Parent : {}", id);
        parentRepository.deleteById(id);
    }
}
