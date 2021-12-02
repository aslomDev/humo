package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumoCardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumoCardDTO.class);
        HumoCardDTO humoCardDTO1 = new HumoCardDTO();
        humoCardDTO1.setId(1L);
        HumoCardDTO humoCardDTO2 = new HumoCardDTO();
        assertThat(humoCardDTO1).isNotEqualTo(humoCardDTO2);
        humoCardDTO2.setId(humoCardDTO1.getId());
        assertThat(humoCardDTO1).isEqualTo(humoCardDTO2);
        humoCardDTO2.setId(2L);
        assertThat(humoCardDTO1).isNotEqualTo(humoCardDTO2);
        humoCardDTO1.setId(null);
        assertThat(humoCardDTO1).isNotEqualTo(humoCardDTO2);
    }
}
