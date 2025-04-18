package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.FeedbackAsserts.*;
import static com.bzu.smartvax.domain.FeedbackTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedbackMapperTest {

    private FeedbackMapper feedbackMapper;

    @BeforeEach
    void setUp() {
        feedbackMapper = new FeedbackMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeedbackSample1();
        var actual = feedbackMapper.toEntity(feedbackMapper.toDto(expected));
        assertFeedbackAllPropertiesEquals(expected, actual);
    }
}
