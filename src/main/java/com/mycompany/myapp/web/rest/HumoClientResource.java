package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.HumoClientRepository;
import com.mycompany.myapp.service.HumoClientService;
import com.mycompany.myapp.service.dto.HumoClientDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HumoClient}.
 */
@RestController
@RequestMapping("/api")
public class HumoClientResource {

    private final Logger log = LoggerFactory.getLogger(HumoClientResource.class);

    private static final String ENTITY_NAME = "humoClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HumoClientService humoClientService;

    private final HumoClientRepository humoClientRepository;

    public HumoClientResource(HumoClientService humoClientService, HumoClientRepository humoClientRepository) {
        this.humoClientService = humoClientService;
        this.humoClientRepository = humoClientRepository;
    }

    /**
     * {@code POST  /humo-clients} : Create a new humoClient.
     *
     * @param humoClientDTO the humoClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new humoClientDTO, or with status {@code 400 (Bad Request)} if the humoClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/humo-clients")
    public Mono<ResponseEntity<HumoClientDTO>> createHumoClient(@RequestBody HumoClientDTO humoClientDTO) throws URISyntaxException {
        log.debug("REST request to save HumoClient : {}", humoClientDTO);
        if (humoClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new humoClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return humoClientService
            .save(humoClientDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/humo-clients/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /humo-clients/:id} : Updates an existing humoClient.
     *
     * @param id the id of the humoClientDTO to save.
     * @param humoClientDTO the humoClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humoClientDTO,
     * or with status {@code 400 (Bad Request)} if the humoClientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the humoClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/humo-clients/{id}")
    public Mono<ResponseEntity<HumoClientDTO>> updateHumoClient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HumoClientDTO humoClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HumoClient : {}, {}", id, humoClientDTO);
        if (humoClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humoClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return humoClientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return humoClientService
                    .save(humoClientDTO)
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
     * {@code PATCH  /humo-clients/:id} : Partial updates given fields of an existing humoClient, field will ignore if it is null
     *
     * @param id the id of the humoClientDTO to save.
     * @param humoClientDTO the humoClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humoClientDTO,
     * or with status {@code 400 (Bad Request)} if the humoClientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the humoClientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the humoClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/humo-clients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HumoClientDTO>> partialUpdateHumoClient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HumoClientDTO humoClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HumoClient partially : {}, {}", id, humoClientDTO);
        if (humoClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humoClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return humoClientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HumoClientDTO> result = humoClientService.partialUpdate(humoClientDTO);

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
     * {@code GET  /humo-clients} : get all the humoClients.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of humoClients in body.
     */
    @GetMapping("/humo-clients")
    public Mono<ResponseEntity<List<HumoClientDTO>>> getAllHumoClients(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of HumoClients");
        return humoClientService
            .countAll()
            .zipWith(humoClientService.findAll(pageable).collectList())
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
     * {@code GET  /humo-clients/:id} : get the "id" humoClient.
     *
     * @param id the id of the humoClientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the humoClientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/humo-clients/{id}")
    public Mono<ResponseEntity<HumoClientDTO>> getHumoClient(@PathVariable Long id) {
        log.debug("REST request to get HumoClient : {}", id);
        Mono<HumoClientDTO> humoClientDTO = humoClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(humoClientDTO);
    }

    /**
     * {@code DELETE  /humo-clients/:id} : delete the "id" humoClient.
     *
     * @param id the id of the humoClientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/humo-clients/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteHumoClient(@PathVariable Long id) {
        log.debug("REST request to delete HumoClient : {}", id);
        return humoClientService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
