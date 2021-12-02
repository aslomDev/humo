package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.repository.ClientRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Client}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private final ClientRepository clientRepository;

    public ClientResource(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("/clients")
    public Mono<List<Client>> getAllClients() {
        log.debug("REST request to get all Clients");
        return clientRepository.findAll().collectList();
    }

    /**
     * {@code GET  /clients} : get all the clients as a stream.
     * @return the {@link Flux} of clients.
     */
    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Client> getAllClientsAsStream() {
        log.debug("REST request to get all Clients as a stream");
        return clientRepository.findAll();
    }

    /**
     * {@code GET  /clients/:id} : get the "id" client.
     *
     * @param id the id of the client to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the client, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public Mono<ResponseEntity<Client>> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Mono<Client> client = clientRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(client);
    }
}
