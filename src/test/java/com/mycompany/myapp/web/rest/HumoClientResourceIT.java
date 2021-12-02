package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HumoClient;
import com.mycompany.myapp.repository.HumoClientRepository;
import com.mycompany.myapp.service.EntityManager;
import com.mycompany.myapp.service.dto.HumoClientDTO;
import com.mycompany.myapp.service.mapper.HumoClientMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link HumoClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class HumoClientResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/humo-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HumoClientRepository humoClientRepository;

    @Autowired
    private HumoClientMapper humoClientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HumoClient humoClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumoClient createEntity(EntityManager em) {
        HumoClient humoClient = new HumoClient().name(DEFAULT_NAME).phone(DEFAULT_PHONE).address(DEFAULT_ADDRESS);
        return humoClient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumoClient createUpdatedEntity(EntityManager em) {
        HumoClient humoClient = new HumoClient().name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS);
        return humoClient;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HumoClient.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        humoClient = createEntity(em);
    }

    @Test
    void createHumoClient() throws Exception {
        int databaseSizeBeforeCreate = humoClientRepository.findAll().collectList().block().size();
        // Create the HumoClient
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeCreate + 1);
        HumoClient testHumoClient = humoClientList.get(humoClientList.size() - 1);
        assertThat(testHumoClient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHumoClient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testHumoClient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    void createHumoClientWithExistingId() throws Exception {
        // Create the HumoClient with an existing ID
        humoClient.setId(1L);
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);

        int databaseSizeBeforeCreate = humoClientRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHumoClients() {
        // Initialize the database
        humoClientRepository.save(humoClient).block();

        // Get all the humoClientList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(humoClient.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS));
    }

    @Test
    void getHumoClient() {
        // Initialize the database
        humoClientRepository.save(humoClient).block();

        // Get the humoClient
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, humoClient.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(humoClient.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS));
    }

    @Test
    void getNonExistingHumoClient() {
        // Get the humoClient
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewHumoClient() throws Exception {
        // Initialize the database
        humoClientRepository.save(humoClient).block();

        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();

        // Update the humoClient
        HumoClient updatedHumoClient = humoClientRepository.findById(humoClient.getId()).block();
        updatedHumoClient.name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS);
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(updatedHumoClient);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, humoClientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
        HumoClient testHumoClient = humoClientList.get(humoClientList.size() - 1);
        assertThat(testHumoClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHumoClient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testHumoClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    void putNonExistingHumoClient() throws Exception {
        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();
        humoClient.setId(count.incrementAndGet());

        // Create the HumoClient
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, humoClientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHumoClient() throws Exception {
        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();
        humoClient.setId(count.incrementAndGet());

        // Create the HumoClient
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHumoClient() throws Exception {
        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();
        humoClient.setId(count.incrementAndGet());

        // Create the HumoClient
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHumoClientWithPatch() throws Exception {
        // Initialize the database
        humoClientRepository.save(humoClient).block();

        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();

        // Update the humoClient using partial update
        HumoClient partialUpdatedHumoClient = new HumoClient();
        partialUpdatedHumoClient.setId(humoClient.getId());

        partialUpdatedHumoClient.name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHumoClient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHumoClient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
        HumoClient testHumoClient = humoClientList.get(humoClientList.size() - 1);
        assertThat(testHumoClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHumoClient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testHumoClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    void fullUpdateHumoClientWithPatch() throws Exception {
        // Initialize the database
        humoClientRepository.save(humoClient).block();

        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();

        // Update the humoClient using partial update
        HumoClient partialUpdatedHumoClient = new HumoClient();
        partialUpdatedHumoClient.setId(humoClient.getId());

        partialUpdatedHumoClient.name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHumoClient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHumoClient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
        HumoClient testHumoClient = humoClientList.get(humoClientList.size() - 1);
        assertThat(testHumoClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHumoClient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testHumoClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    void patchNonExistingHumoClient() throws Exception {
        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();
        humoClient.setId(count.incrementAndGet());

        // Create the HumoClient
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, humoClientDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHumoClient() throws Exception {
        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();
        humoClient.setId(count.incrementAndGet());

        // Create the HumoClient
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHumoClient() throws Exception {
        int databaseSizeBeforeUpdate = humoClientRepository.findAll().collectList().block().size();
        humoClient.setId(count.incrementAndGet());

        // Create the HumoClient
        HumoClientDTO humoClientDTO = humoClientMapper.toDto(humoClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoClientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HumoClient in the database
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHumoClient() {
        // Initialize the database
        humoClientRepository.save(humoClient).block();

        int databaseSizeBeforeDelete = humoClientRepository.findAll().collectList().block().size();

        // Delete the humoClient
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, humoClient.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HumoClient> humoClientList = humoClientRepository.findAll().collectList().block();
        assertThat(humoClientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
