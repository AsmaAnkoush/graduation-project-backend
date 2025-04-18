package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.ParentAsserts.*;
import static com.bzu.smartvax.domain.ParentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParentMapperTest {

    private ParentMapper parentMapper;

    @BeforeEach
    void setUp() {
        parentMapper = new ParentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getParentSample1();
        var actual = parentMapper.toEntity(parentMapper.toDto(expected));
        assertParentAllPropertiesEquals(expected, actual);
    }
}
