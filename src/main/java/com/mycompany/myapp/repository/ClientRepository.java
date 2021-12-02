package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends R2dbcRepository<Client, Long>, ClientRepositoryInternal {
    // just to avoid having unambigous methods
    @Override
    Flux<Client> findAll();

    @Override
    Mono<Client> findById(Long id);

    @Override
    <S extends Client> Mono<S> save(S entity);
}

interface ClientRepositoryInternal {
    <S extends Client> Mono<S> insert(S entity);
    <S extends Client> Mono<S> save(S entity);
    Mono<Integer> update(Client entity);

    Flux<Client> findAll();
    Mono<Client> findById(Long id);
    Flux<Client> findAllBy(Pageable pageable);
    Flux<Client> findAllBy(Pageable pageable, Criteria criteria);
}
