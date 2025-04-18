package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.ScheduleVaccinationAsserts.*;
import static com.bzu.smartvax.domain.ScheduleVaccinationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScheduleVaccinationMapperTest {

    private ScheduleVaccinationMapper scheduleVaccinationMapper;

    @BeforeEach
    void setUp() {
        scheduleVaccinationMapper = new ScheduleVaccinationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScheduleVaccinationSample1();
        var actual = scheduleVaccinationMapper.toEntity(scheduleVaccinationMapper.toDto(expected));
        assertScheduleVaccinationAllPropertiesEquals(expected, actual);
    }
}
