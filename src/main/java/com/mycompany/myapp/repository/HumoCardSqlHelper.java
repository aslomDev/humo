package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class HumoCardSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("card_number", table, columnPrefix + "_card_number"));
        columns.add(Column.aliased("bank_number", table, columnPrefix + "_bank_number"));
        columns.add(Column.aliased("sys_number", table, columnPrefix + "_sys_number"));
        columns.add(Column.aliased("card_type", table, columnPrefix + "_card_type"));
        columns.add(Column.aliased("credit", table, columnPrefix + "_credit"));
        columns.add(Column.aliased("balance", table, columnPrefix + "_balance"));
        columns.add(Column.aliased("expire_date", table, columnPrefix + "_expire_date"));

        columns.add(Column.aliased("humo_client_id", table, columnPrefix + "_humo_client_id"));
        return columns;
    }
}
