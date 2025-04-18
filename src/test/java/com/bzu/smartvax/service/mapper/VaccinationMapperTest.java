package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.VaccinationAsserts.*;
import static com.bzu.smartvax.domain.VaccinationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccinationMapperTest {

    private VaccinationMapper vaccinationMapper;

    @BeforeEach
    void setUp() {
        vaccinationMapper = new VaccinationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVaccinationSample1();
        var actual = vaccinationMapper.toEntity(vaccinationMapper.toDto(expected));
        assertVaccinationAllPropertiesEquals(expected, actual);
    }
}
