package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.HumoCardRepository;
import com.mycompany.myapp.service.HumoCardService;
import com.mycompany.myapp.service.dto.HumoCardDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.HumoCard}.
 */
@RestController
@RequestMapping("/api")
public class HumoCardResource {

    private final Logger log = LoggerFactory.getLogger(HumoCardResource.class);

    private static final String ENTITY_NAME = "humoCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HumoCardService humoCardService;

    private final HumoCardRepository humoCardRepository;

    public HumoCardResource(HumoCardService humoCardService, HumoCardRepository humoCardRepository) {
        this.humoCardService = humoCardService;
        this.humoCardRepository = humoCardRepository;
    }

    /**
     * {@code POST  /humo-cards} : Create a new humoCard.
     *
     * @param humoCardDTO the humoCardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new humoCardDTO, or with status {@code 400 (Bad Request)} if the humoCard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/humo-cards")
    public Mono<ResponseEntity<HumoCardDTO>> createHumoCard(@RequestBody HumoCardDTO humoCardDTO) throws URISyntaxException {
        log.debug("REST request to save HumoCard : {}", humoCardDTO);
        if (humoCardDTO.getId() != null) {
            throw new BadRequestAlertException("A new humoCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return humoCardService
            .save(humoCardDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/humo-cards/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /humo-cards/:id} : Updates an existing humoCard.
     *
     * @param id the id of the humoCardDTO to save.
     * @param humoCardDTO the humoCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humoCardDTO,
     * or with status {@code 400 (Bad Request)} if the humoCardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the humoCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/humo-cards/{id}")
    public Mono<ResponseEntity<HumoCardDTO>> updateHumoCard(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HumoCardDTO humoCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HumoCard : {}, {}", id, humoCardDTO);
        if (humoCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humoCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return humoCardRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return humoCardService
                    .save(humoCardDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /humo-cards/:id} : Partial updates given fields of an existing humoCard, field will ignore if it is null
     *
     * @param id the id of the humoCardDTO to save.
     * @param humoCardDTO the humoCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humoCardDTO,
     * or with status {@code 400 (Bad Request)} if the humoCardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the humoCardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the humoCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/humo-cards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HumoCardDTO>> partialUpdateHumoCard(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HumoCardDTO humoCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HumoCard partially : {}, {}", id, humoCardDTO);
        if (humoCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humoCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return humoCardRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HumoCardDTO> result = humoCardService.partialUpdate(humoCardDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /humo-cards} : get all the humoCards.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of humoCards in body.
     */
    @GetMapping("/humo-cards")
    public Mono<ResponseEntity<List<HumoCardDTO>>> getAllHumoCards(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of HumoCards");
        return humoCardService
            .countAll()
            .zipWith(humoCardService.findAll(pageable).collectList())
            .map(countWithEntities -> {
                return ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2());
            });
    }

    /**
     * {@code GET  /humo-cards/:id} : get the "id" humoCard.
     *
     * @param id the id of the humoCardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the humoCardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/humo-cards/{id}")
    public Mono<ResponseEntity<HumoCardDTO>> getHumoCard(@PathVariable Long id) {
        log.debug("REST request to get HumoCard : {}", id);
        Mono<HumoCardDTO> humoCardDTO = humoCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(humoCardDTO);
    }

    /**
     * {@code DELETE  /humo-cards/:id} : delete the "id" humoCard.
     *
     * @param id the id of the humoCardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/humo-cards/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteHumoCard(@PathVariable Long id) {
        log.debug("REST request to delete HumoCard : {}", id);
        return humoCardService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
