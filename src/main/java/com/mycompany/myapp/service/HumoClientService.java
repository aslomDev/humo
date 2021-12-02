package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.HumoClientDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.HumoClient}.
 */
public interface HumoClientService {
    /**
     * Save a humoClient.
     *
     * @param humoClientDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HumoClientDTO> save(HumoClientDTO humoClientDTO);

    /**
     * Partially updates a humoClient.
     *
     * @param humoClientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HumoClientDTO> partialUpdate(HumoClientDTO humoClientDTO);

    /**
     * Get all the humoClients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HumoClientDTO> findAll(Pageable pageable);

    /**
     * Returns the number of humoClients available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" humoClient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HumoClientDTO> findOne(Long id);

    /**
     * Delete the "id" humoClient.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
