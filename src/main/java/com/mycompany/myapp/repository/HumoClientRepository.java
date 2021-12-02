package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HumoClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the HumoClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HumoClientRepository extends R2dbcRepository<HumoClient, Long>, HumoClientRepositoryInternal {
    Flux<HumoClient> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<HumoClient> findAll();

    @Override
    Mono<HumoClient> findById(Long id);

    @Override
    <S extends HumoClient> Mono<S> save(S entity);
}

interface HumoClientRepositoryInternal {
    <S extends HumoClient> Mono<S> insert(S entity);
    <S extends HumoClient> Mono<S> save(S entity);
    Mono<Integer> update(HumoClient entity);

    Flux<HumoClient> findAll();
    Mono<HumoClient> findById(Long id);
    Flux<HumoClient> findAllBy(Pageable pageable);
    Flux<HumoClient> findAllBy(Pageable pageable, Criteria criteria);
}
