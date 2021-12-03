package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HumoCard;
import com.mycompany.myapp.domain.enumeration.HUMO;
import com.mycompany.myapp.repository.HumoCardRepository;
import com.mycompany.myapp.service.EntityManager;
import com.mycompany.myapp.service.dto.HumoCardDTO;
import com.mycompany.myapp.service.mapper.HumoCardMapper;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link HumoCardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class HumoCardResourceIT {

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SYS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SYS_NUMBER = "BBBBBBBBBB";

    private static final HUMO DEFAULT_CARD_TYPE = HUMO.HUMO;
    private static final HUMO UPDATED_CARD_TYPE = HUMO.HUMO;

    private static final Boolean DEFAULT_CREDIT = false;
    private static final Boolean UPDATED_CREDIT = true;

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final LocalDate DEFAULT_EXPIRE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PAN = "AAAAAAAAAA";
    private static final String UPDATED_PAN = "BBBBBBBBBB";

    private static final String DEFAULT_MASKED_PAN = "AAAAAAAAAA";
    private static final String UPDATED_MASKED_PAN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/humo-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HumoCardRepository humoCardRepository;

    @Autowired
    private HumoCardMapper humoCardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HumoCard humoCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumoCard createEntity(EntityManager em) {
        HumoCard humoCard = new HumoCard()
            .cardNumber(DEFAULT_CARD_NUMBER)
            .bankNumber(DEFAULT_BANK_NUMBER)
            .sysNumber(DEFAULT_SYS_NUMBER)
            .cardType(DEFAULT_CARD_TYPE)
            .credit(DEFAULT_CREDIT)
            .balance(DEFAULT_BALANCE)
            .expireDate(DEFAULT_EXPIRE_DATE)
            .pan(DEFAULT_PAN)
            .maskedPan(DEFAULT_MASKED_PAN);
        return humoCard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumoCard createUpdatedEntity(EntityManager em) {
        HumoCard humoCard = new HumoCard()
            .cardNumber(UPDATED_CARD_NUMBER)
            .bankNumber(UPDATED_BANK_NUMBER)
            .sysNumber(UPDATED_SYS_NUMBER)
            .cardType(UPDATED_CARD_TYPE)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .pan(UPDATED_PAN)
            .maskedPan(UPDATED_MASKED_PAN);
        return humoCard;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HumoCard.class).block();
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
        humoCard = createEntity(em);
    }

    @Test
    void createHumoCard() throws Exception {
        int databaseSizeBeforeCreate = humoCardRepository.findAll().collectList().block().size();
        // Create the HumoCard
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeCreate + 1);
        HumoCard testHumoCard = humoCardList.get(humoCardList.size() - 1);
        assertThat(testHumoCard.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testHumoCard.getBankNumber()).isEqualTo(DEFAULT_BANK_NUMBER);
        assertThat(testHumoCard.getSysNumber()).isEqualTo(DEFAULT_SYS_NUMBER);
        assertThat(testHumoCard.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testHumoCard.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testHumoCard.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testHumoCard.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testHumoCard.getPan()).isEqualTo(DEFAULT_PAN);
        assertThat(testHumoCard.getMaskedPan()).isEqualTo(DEFAULT_MASKED_PAN);
    }

    @Test
    void createHumoCardWithExistingId() throws Exception {
        // Create the HumoCard with an existing ID
        humoCard.setId(1L);
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);

        int databaseSizeBeforeCreate = humoCardRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHumoCards() {
        // Initialize the database
        humoCardRepository.save(humoCard).block();

        // Get all the humoCardList
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
            .value(hasItem(humoCard.getId().intValue()))
            .jsonPath("$.[*].cardNumber")
            .value(hasItem(DEFAULT_CARD_NUMBER))
            .jsonPath("$.[*].bankNumber")
            .value(hasItem(DEFAULT_BANK_NUMBER))
            .jsonPath("$.[*].sysNumber")
            .value(hasItem(DEFAULT_SYS_NUMBER))
            .jsonPath("$.[*].cardType")
            .value(hasItem(DEFAULT_CARD_TYPE.toString()))
            .jsonPath("$.[*].credit")
            .value(hasItem(DEFAULT_CREDIT.booleanValue()))
            .jsonPath("$.[*].balance")
            .value(hasItem(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.[*].expireDate")
            .value(hasItem(DEFAULT_EXPIRE_DATE.toString()))
            .jsonPath("$.[*].pan")
            .value(hasItem(DEFAULT_PAN))
            .jsonPath("$.[*].maskedPan")
            .value(hasItem(DEFAULT_MASKED_PAN));
    }

    @Test
    void getHumoCard() {
        // Initialize the database
        humoCardRepository.save(humoCard).block();

        // Get the humoCard
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, humoCard.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(humoCard.getId().intValue()))
            .jsonPath("$.cardNumber")
            .value(is(DEFAULT_CARD_NUMBER))
            .jsonPath("$.bankNumber")
            .value(is(DEFAULT_BANK_NUMBER))
            .jsonPath("$.sysNumber")
            .value(is(DEFAULT_SYS_NUMBER))
            .jsonPath("$.cardType")
            .value(is(DEFAULT_CARD_TYPE.toString()))
            .jsonPath("$.credit")
            .value(is(DEFAULT_CREDIT.booleanValue()))
            .jsonPath("$.balance")
            .value(is(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.expireDate")
            .value(is(DEFAULT_EXPIRE_DATE.toString()))
            .jsonPath("$.pan")
            .value(is(DEFAULT_PAN))
            .jsonPath("$.maskedPan")
            .value(is(DEFAULT_MASKED_PAN));
    }

    @Test
    void getNonExistingHumoCard() {
        // Get the humoCard
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewHumoCard() throws Exception {
        // Initialize the database
        humoCardRepository.save(humoCard).block();

        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();

        // Update the humoCard
        HumoCard updatedHumoCard = humoCardRepository.findById(humoCard.getId()).block();
        updatedHumoCard
            .cardNumber(UPDATED_CARD_NUMBER)
            .bankNumber(UPDATED_BANK_NUMBER)
            .sysNumber(UPDATED_SYS_NUMBER)
            .cardType(UPDATED_CARD_TYPE)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .pan(UPDATED_PAN)
            .maskedPan(UPDATED_MASKED_PAN);
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(updatedHumoCard);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, humoCardDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
        HumoCard testHumoCard = humoCardList.get(humoCardList.size() - 1);
        assertThat(testHumoCard.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testHumoCard.getBankNumber()).isEqualTo(UPDATED_BANK_NUMBER);
        assertThat(testHumoCard.getSysNumber()).isEqualTo(UPDATED_SYS_NUMBER);
        assertThat(testHumoCard.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testHumoCard.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testHumoCard.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testHumoCard.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testHumoCard.getPan()).isEqualTo(UPDATED_PAN);
        assertThat(testHumoCard.getMaskedPan()).isEqualTo(UPDATED_MASKED_PAN);
    }

    @Test
    void putNonExistingHumoCard() throws Exception {
        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();
        humoCard.setId(count.incrementAndGet());

        // Create the HumoCard
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, humoCardDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHumoCard() throws Exception {
        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();
        humoCard.setId(count.incrementAndGet());

        // Create the HumoCard
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHumoCard() throws Exception {
        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();
        humoCard.setId(count.incrementAndGet());

        // Create the HumoCard
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHumoCardWithPatch() throws Exception {
        // Initialize the database
        humoCardRepository.save(humoCard).block();

        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();

        // Update the humoCard using partial update
        HumoCard partialUpdatedHumoCard = new HumoCard();
        partialUpdatedHumoCard.setId(humoCard.getId());

        partialUpdatedHumoCard
            .cardNumber(UPDATED_CARD_NUMBER)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .pan(UPDATED_PAN)
            .maskedPan(UPDATED_MASKED_PAN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHumoCard.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHumoCard))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
        HumoCard testHumoCard = humoCardList.get(humoCardList.size() - 1);
        assertThat(testHumoCard.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testHumoCard.getBankNumber()).isEqualTo(DEFAULT_BANK_NUMBER);
        assertThat(testHumoCard.getSysNumber()).isEqualTo(DEFAULT_SYS_NUMBER);
        assertThat(testHumoCard.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testHumoCard.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testHumoCard.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testHumoCard.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testHumoCard.getPan()).isEqualTo(UPDATED_PAN);
        assertThat(testHumoCard.getMaskedPan()).isEqualTo(UPDATED_MASKED_PAN);
    }

    @Test
    void fullUpdateHumoCardWithPatch() throws Exception {
        // Initialize the database
        humoCardRepository.save(humoCard).block();

        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();

        // Update the humoCard using partial update
        HumoCard partialUpdatedHumoCard = new HumoCard();
        partialUpdatedHumoCard.setId(humoCard.getId());

        partialUpdatedHumoCard
            .cardNumber(UPDATED_CARD_NUMBER)
            .bankNumber(UPDATED_BANK_NUMBER)
            .sysNumber(UPDATED_SYS_NUMBER)
            .cardType(UPDATED_CARD_TYPE)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .pan(UPDATED_PAN)
            .maskedPan(UPDATED_MASKED_PAN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHumoCard.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHumoCard))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
        HumoCard testHumoCard = humoCardList.get(humoCardList.size() - 1);
        assertThat(testHumoCard.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testHumoCard.getBankNumber()).isEqualTo(UPDATED_BANK_NUMBER);
        assertThat(testHumoCard.getSysNumber()).isEqualTo(UPDATED_SYS_NUMBER);
        assertThat(testHumoCard.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testHumoCard.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testHumoCard.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testHumoCard.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testHumoCard.getPan()).isEqualTo(UPDATED_PAN);
        assertThat(testHumoCard.getMaskedPan()).isEqualTo(UPDATED_MASKED_PAN);
    }

    @Test
    void patchNonExistingHumoCard() throws Exception {
        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();
        humoCard.setId(count.incrementAndGet());

        // Create the HumoCard
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, humoCardDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHumoCard() throws Exception {
        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();
        humoCard.setId(count.incrementAndGet());

        // Create the HumoCard
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHumoCard() throws Exception {
        int databaseSizeBeforeUpdate = humoCardRepository.findAll().collectList().block().size();
        humoCard.setId(count.incrementAndGet());

        // Create the HumoCard
        HumoCardDTO humoCardDTO = humoCardMapper.toDto(humoCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(humoCardDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HumoCard in the database
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHumoCard() {
        // Initialize the database
        humoCardRepository.save(humoCard).block();

        int databaseSizeBeforeDelete = humoCardRepository.findAll().collectList().block().size();

        // Delete the humoCard
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, humoCard.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HumoCard> humoCardList = humoCardRepository.findAll().collectList().block();
        assertThat(humoCardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
