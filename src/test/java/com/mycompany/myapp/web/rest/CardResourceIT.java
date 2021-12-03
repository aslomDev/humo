package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Card;
import com.mycompany.myapp.domain.enumeration.CardType;
import com.mycompany.myapp.repository.CardRepository;
import com.mycompany.myapp.service.EntityManager;
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
 * Integration tests for the {@link CardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class CardResourceIT {

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SYS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SYS_NUMBER = "BBBBBBBBBB";

    private static final CardType DEFAULT_TYPE_CARD = CardType.HUMO;
    private static final CardType UPDATED_TYPE_CARD = CardType.HUMO;

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

    private static final String ENTITY_API_URL = "/api/cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Card card;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
            .cardNumber(DEFAULT_CARD_NUMBER)
            .bankNumber(DEFAULT_BANK_NUMBER)
            .sysNumber(DEFAULT_SYS_NUMBER)
            .typeCard(DEFAULT_TYPE_CARD)
            .credit(DEFAULT_CREDIT)
            .balance(DEFAULT_BALANCE)
            .expireDate(DEFAULT_EXPIRE_DATE)
            .pan(DEFAULT_PAN)
            .maskedPan(DEFAULT_MASKED_PAN);
        return card;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createUpdatedEntity(EntityManager em) {
        Card card = new Card()
            .cardNumber(UPDATED_CARD_NUMBER)
            .bankNumber(UPDATED_BANK_NUMBER)
            .sysNumber(UPDATED_SYS_NUMBER)
            .typeCard(UPDATED_TYPE_CARD)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .pan(UPDATED_PAN)
            .maskedPan(UPDATED_MASKED_PAN);
        return card;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Card.class).block();
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
        card = createEntity(em);
    }

    @Test
    void getAllCardsAsStream() {
        // Initialize the database
        cardRepository.save(card).block();

        List<Card> cardList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Card.class)
            .getResponseBody()
            .filter(card::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(cardList).isNotNull();
        assertThat(cardList).hasSize(1);
        Card testCard = cardList.get(0);
        assertThat(testCard.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testCard.getBankNumber()).isEqualTo(DEFAULT_BANK_NUMBER);
        assertThat(testCard.getSysNumber()).isEqualTo(DEFAULT_SYS_NUMBER);
        assertThat(testCard.getTypeCard()).isEqualTo(DEFAULT_TYPE_CARD);
        assertThat(testCard.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testCard.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testCard.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testCard.getPan()).isEqualTo(DEFAULT_PAN);
        assertThat(testCard.getMaskedPan()).isEqualTo(DEFAULT_MASKED_PAN);
    }

    @Test
    void getAllCards() {
        // Initialize the database
        cardRepository.save(card).block();

        // Get all the cardList
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
            .value(hasItem(card.getId().intValue()))
            .jsonPath("$.[*].cardNumber")
            .value(hasItem(DEFAULT_CARD_NUMBER))
            .jsonPath("$.[*].bankNumber")
            .value(hasItem(DEFAULT_BANK_NUMBER))
            .jsonPath("$.[*].sysNumber")
            .value(hasItem(DEFAULT_SYS_NUMBER))
            .jsonPath("$.[*].typeCard")
            .value(hasItem(DEFAULT_TYPE_CARD.toString()))
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
    void getCard() {
        // Initialize the database
        cardRepository.save(card).block();

        // Get the card
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, card.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(card.getId().intValue()))
            .jsonPath("$.cardNumber")
            .value(is(DEFAULT_CARD_NUMBER))
            .jsonPath("$.bankNumber")
            .value(is(DEFAULT_BANK_NUMBER))
            .jsonPath("$.sysNumber")
            .value(is(DEFAULT_SYS_NUMBER))
            .jsonPath("$.typeCard")
            .value(is(DEFAULT_TYPE_CARD.toString()))
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
    void getNonExistingCard() {
        // Get the card
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }
}
