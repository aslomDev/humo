package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumoClientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumoClientDTO.class);
        HumoClientDTO humoClientDTO1 = new HumoClientDTO();
        humoClientDTO1.setId(1L);
        HumoClientDTO humoClientDTO2 = new HumoClientDTO();
        assertThat(humoClientDTO1).isNotEqualTo(humoClientDTO2);
        humoClientDTO2.setId(humoClientDTO1.getId());
        assertThat(humoClientDTO1).isEqualTo(humoClientDTO2);
        humoClientDTO2.setId(2L);
        assertThat(humoClientDTO1).isNotEqualTo(humoClientDTO2);
        humoClientDTO1.setId(null);
        assertThat(humoClientDTO1).isNotEqualTo(humoClientDTO2);
    }
}
