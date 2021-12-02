package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumoClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumoClient.class);
        HumoClient humoClient1 = new HumoClient();
        humoClient1.setId(1L);
        HumoClient humoClient2 = new HumoClient();
        humoClient2.setId(humoClient1.getId());
        assertThat(humoClient1).isEqualTo(humoClient2);
        humoClient2.setId(2L);
        assertThat(humoClient1).isNotEqualTo(humoClient2);
        humoClient1.setId(null);
        assertThat(humoClient1).isNotEqualTo(humoClient2);
    }
}
