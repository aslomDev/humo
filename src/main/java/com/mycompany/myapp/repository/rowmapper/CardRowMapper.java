package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Card;
import com.mycompany.myapp.domain.enumeration.CardType;
import com.mycompany.myapp.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Card}, with proper type conversions.
 */
@Service
public class CardRowMapper implements BiFunction<Row, String, Card> {

    private final ColumnConverter converter;

    public CardRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Card} stored in the database.
     */
    @Override
    public Card apply(Row row, String prefix) {
        Card entity = new Card();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCardNumber(converter.fromRow(row, prefix + "_card_number", String.class));
        entity.setBankNumber(converter.fromRow(row, prefix + "_bank_number", String.class));
        entity.setSysNumber(converter.fromRow(row, prefix + "_sys_number", String.class));
        entity.setTypeCard(converter.fromRow(row, prefix + "_type_card", CardType.class));
        entity.setCredit(converter.fromRow(row, prefix + "_credit", Boolean.class));
        entity.setBalance(converter.fromRow(row, prefix + "_balance", BigDecimal.class));
        entity.setExpireDate(converter.fromRow(row, prefix + "_expire_date", LocalDate.class));
        entity.setCardId(converter.fromRow(row, prefix + "_card_id", Long.class));
        return entity;
    }
}
