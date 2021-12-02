package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HumoCardMapperTest {

    private HumoCardMapper humoCardMapper;

    @BeforeEach
    public void setUp() {
        humoCardMapper = new HumoCardMapperImpl();
    }
}
