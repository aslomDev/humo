package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.HumoCard;
import com.mycompany.myapp.domain.enumeration.HUMO;
import com.mycompany.myapp.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HumoCard}, with proper type conversions.
 */
@Service
public class HumoCardRowMapper implements BiFunction<Row, String, HumoCard> {

    private final ColumnConverter converter;

    public HumoCardRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HumoCard} stored in the database.
     */
    @Override
    public HumoCard apply(Row row, String prefix) {
        HumoCard entity = new HumoCard();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCardNumber(converter.fromRow(row, prefix + "_card_number", String.class));
        entity.setBankNumber(converter.fromRow(row, prefix + "_bank_number", String.class));
        entity.setSysNumber(converter.fromRow(row, prefix + "_sys_number", String.class));
        entity.setCardType(converter.fromRow(row, prefix + "_card_type", HUMO.class));
        entity.setCredit(converter.fromRow(row, prefix + "_credit", Boolean.class));
        entity.setBalance(converter.fromRow(row, prefix + "_balance", BigDecimal.class));
        entity.setExpireDate(converter.fromRow(row, prefix + "_expire_date", LocalDate.class));
        entity.setPan(converter.fromRow(row, prefix + "_pan", String.class));
        entity.setMaskedPan(converter.fromRow(row, prefix + "_masked_pan", String.class));
        entity.setHumoClientId(converter.fromRow(row, prefix + "_humo_client_id", Long.class));
        return entity;
    }
}
