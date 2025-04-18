package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.HealthWorkerAsserts.*;
import static com.bzu.smartvax.domain.HealthWorkerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HealthWorkerMapperTest {

    private HealthWorkerMapper healthWorkerMapper;

    @BeforeEach
    void setUp() {
        healthWorkerMapper = new HealthWorkerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHealthWorkerSample1();
        var actual = healthWorkerMapper.toEntity(healthWorkerMapper.toDto(expected));
        assertHealthWorkerAllPropertiesEquals(expected, actual);
    }
}
