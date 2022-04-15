package com.tutrit.sqlinjection.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CreateTableUseCase {

    final Connection connection;
    final Pattern ONLY_LETTER_OR_SPACE = Pattern.compile("[()a-zA-Z,_\\s\\d]*");

    public CreateTableUseCase(final Connection connection) {
        this.connection = connection;
    }

    public void createTable(String tableName, Map<String, String> columns) {
        try {
            StringBuffer sql = new StringBuffer("create table ");
            sql.append(tableName);
            sql.append("(");

            columns.forEach((column, type) -> {
                sql.append(column);
                sql.append(" ");
                sql.append(type);
                sql.append(",");
            });

            sql.replace(sql.lastIndexOf(","), sql.length(), ");");
            Statement st = connection.createStatement();
            st.execute(sql.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createQuery(String tableName, Map<String, String> columns) {
        StringBuffer sql = new StringBuffer("create table ");
        sql.append(tableName);
        sql.append("(");

        columns.forEach((column, type) -> {
            sql.append(column);
            sql.append(" ");
            sql.append(type);
            sql.append(",");
        });

        sql.replace(sql.lastIndexOf(","), sql.length(), ");");
        return sql.toString();
    }

    public void sanitize(String sql) {
        if (';' == sql.charAt(sql.length() - 1)) {
            if (ONLY_LETTER_OR_SPACE.matcher(sql.substring(0, sql.length() - 2)).matches()) return;
        } else {
            if (ONLY_LETTER_OR_SPACE.matcher(sql).matches()) return;
        }
        throw new RuntimeException("Possible sql injection in " + sql);
    }

    public void execute(String sql) {
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeWithPreparedStatement(String sql) {
        try {
            connection.prepareStatement(sql).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
