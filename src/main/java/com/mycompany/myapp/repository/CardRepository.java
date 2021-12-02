package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepository extends R2dbcRepository<Card, Long>, CardRepositoryInternal {
    @Query("SELECT * FROM card entity WHERE entity.card_id = :id")
    Flux<Card> findByCard(Long id);

    @Query("SELECT * FROM card entity WHERE entity.card_id IS NULL")
    Flux<Card> findAllWhereCardIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Card> findAll();

    @Override
    Mono<Card> findById(Long id);

    @Override
    <S extends Card> Mono<S> save(S entity);
}

interface CardRepositoryInternal {
    <S extends Card> Mono<S> insert(S entity);
    <S extends Card> Mono<S> save(S entity);
    Mono<Integer> update(Card entity);

    Flux<Card> findAll();
    Mono<Card> findById(Long id);
    Flux<Card> findAllBy(Pageable pageable);
    Flux<Card> findAllBy(Pageable pageable, Criteria criteria);
}
