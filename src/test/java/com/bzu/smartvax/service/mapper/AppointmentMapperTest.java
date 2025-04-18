package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.AppointmentAsserts.*;
import static com.bzu.smartvax.domain.AppointmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentMapperTest {

    private AppointmentMapper appointmentMapper;

    @BeforeEach
    void setUp() {
        appointmentMapper = new AppointmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppointmentSample1();
        var actual = appointmentMapper.toEntity(appointmentMapper.toDto(expected));
        assertAppointmentAllPropertiesEquals(expected, actual);
    }
}
