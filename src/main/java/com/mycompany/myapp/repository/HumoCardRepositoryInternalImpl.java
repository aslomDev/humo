package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.mycompany.myapp.domain.HumoCard;
import com.mycompany.myapp.domain.enumeration.HUMO;
import com.mycompany.myapp.repository.rowmapper.HumoCardRowMapper;
import com.mycompany.myapp.repository.rowmapper.HumoClientRowMapper;
import com.mycompany.myapp.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the HumoCard entity.
 */
@SuppressWarnings("unused")
class HumoCardRepositoryInternalImpl implements HumoCardRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HumoClientRowMapper humoclientMapper;
    private final HumoCardRowMapper humocardMapper;

    private static final Table entityTable = Table.aliased("humo_card", EntityManager.ENTITY_ALIAS);
    private static final Table humoClientTable = Table.aliased("humo_client", "humoClient");

    public HumoCardRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HumoClientRowMapper humoclientMapper,
        HumoCardRowMapper humocardMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.humoclientMapper = humoclientMapper;
        this.humocardMapper = humocardMapper;
    }

    @Override
    public Flux<HumoCard> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<HumoCard> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<HumoCard> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = HumoCardSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(HumoClientSqlHelper.getColumns(humoClientTable, "humoClient"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(humoClientTable)
            .on(Column.create("humo_client_id", entityTable))
            .equals(Column.create("id", humoClientTable));

        String select = entityManager.createSelect(selectFrom, HumoCard.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(crit ->
                new StringBuilder(select)
                    .append(" ")
                    .append("WHERE")
                    .append(" ")
                    .append(alias)
                    .append(".")
                    .append(crit.toString())
                    .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<HumoCard> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<HumoCard> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private HumoCard process(Row row, RowMetadata metadata) {
        HumoCard entity = humocardMapper.apply(row, "e");
        entity.setHumoClient(humoclientMapper.apply(row, "humoClient"));
        return entity;
    }

    @Override
    public <S extends HumoCard> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends HumoCard> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update HumoCard with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(HumoCard entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
