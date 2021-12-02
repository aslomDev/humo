package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.HumoClient;
import com.mycompany.myapp.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HumoClient}, with proper type conversions.
 */
@Service
public class HumoClientRowMapper implements BiFunction<Row, String, HumoClient> {

    private final ColumnConverter converter;

    public HumoClientRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HumoClient} stored in the database.
     */
    @Override
    public HumoClient apply(Row row, String prefix) {
        HumoClient entity = new HumoClient();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        return entity;
    }
}
