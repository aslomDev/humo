package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Card;
import com.mycompany.myapp.repository.CardRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Card}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CardResource {

    private final Logger log = LoggerFactory.getLogger(CardResource.class);

    private final CardRepository cardRepository;

    public CardResource(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * {@code GET  /cards} : get all the cards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cards in body.
     */
    @GetMapping("/cards")
    public Mono<List<Card>> getAllCards() {
        log.debug("REST request to get all Cards");
        return cardRepository.findAll().collectList();
    }

    /**
     * {@code GET  /cards} : get all the cards as a stream.
     * @return the {@link Flux} of cards.
     */
    @GetMapping(value = "/cards", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Card> getAllCardsAsStream() {
        log.debug("REST request to get all Cards as a stream");
        return cardRepository.findAll();
    }

    /**
     * {@code GET  /cards/:id} : get the "id" card.
     *
     * @param id the id of the card to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the card, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cards/{id}")
    public Mono<ResponseEntity<Card>> getCard(@PathVariable Long id) {
        log.debug("REST request to get Card : {}", id);
        Mono<Card> card = cardRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(card);
    }
}
