package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumoCardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumoCard.class);
        HumoCard humoCard1 = new HumoCard();
        humoCard1.setId(1L);
        HumoCard humoCard2 = new HumoCard();
        humoCard2.setId(humoCard1.getId());
        assertThat(humoCard1).isEqualTo(humoCard2);
        humoCard2.setId(2L);
        assertThat(humoCard1).isNotEqualTo(humoCard2);
        humoCard1.setId(null);
        assertThat(humoCard1).isNotEqualTo(humoCard2);
    }
}
