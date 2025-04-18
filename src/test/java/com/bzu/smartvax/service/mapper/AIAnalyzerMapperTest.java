package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.AIAnalyzerAsserts.*;
import static com.bzu.smartvax.domain.AIAnalyzerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AIAnalyzerMapperTest {

    private AIAnalyzerMapper aIAnalyzerMapper;

    @BeforeEach
    void setUp() {
        aIAnalyzerMapper = new AIAnalyzerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAIAnalyzerSample1();
        var actual = aIAnalyzerMapper.toEntity(aIAnalyzerMapper.toDto(expected));
        assertAIAnalyzerAllPropertiesEquals(expected, actual);
    }
}
