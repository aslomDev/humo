package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.HumoCardDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.HumoCard}.
 */
public interface HumoCardService {
    /**
     * Save a humoCard.
     *
     * @param humoCardDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HumoCardDTO> save(HumoCardDTO humoCardDTO);

    /**
     * Partially updates a humoCard.
     *
     * @param humoCardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HumoCardDTO> partialUpdate(HumoCardDTO humoCardDTO);

    /**
     * Get all the humoCards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HumoCardDTO> findAll(Pageable pageable);

    /**
     * Returns the number of humoCards available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" humoCard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HumoCardDTO> findOne(Long id);

    /**
     * Delete the "id" humoCard.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
