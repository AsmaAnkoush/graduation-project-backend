package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.service.dto.ParentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parent} and its DTO {@link ParentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParentMapper extends EntityMapper<ParentDTO, Parent> {}
