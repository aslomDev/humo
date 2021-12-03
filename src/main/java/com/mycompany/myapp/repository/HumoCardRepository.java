package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HumoCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the HumoCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HumoCardRepository extends R2dbcRepository<HumoCard, Long>, HumoCardRepositoryInternal {
    Flux<HumoCard> findAllBy(Pageable pageable);

    @Query("SELECT * FROM humo_card entity WHERE entity.humo_client_id = :id")
    Flux<HumoCard> findByHumoClient(Long id);

    @Query("SELECT * FROM humo_card entity WHERE entity.humo_client_id IS NULL")
    Flux<HumoCard> findAllWhereHumoClientIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<HumoCard> findAll();

    @Override
    Mono<HumoCard> findById(Long id);

    Mono<HumoCard> findByPan(String pan);

    Mono<HumoCard> findByMaskedPan(String maskedPan);

    @Override
    <S extends HumoCard> Mono<S> save(S entity);
}

interface HumoCardRepositoryInternal {
    <S extends HumoCard> Mono<S> insert(S entity);
    <S extends HumoCard> Mono<S> save(S entity);
    Mono<Integer> update(HumoCard entity);

    Flux<HumoCard> findAll();
    Mono<HumoCard> findById(Long id);
    Flux<HumoCard> findAllBy(Pageable pageable);
    Flux<HumoCard> findAllBy(Pageable pageable, Criteria criteria);
}
