package com.tutrit.sqlinjection.utils;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;

@Service
public class DatabaseUtil {

    final Connection connection;

    public DatabaseUtil(final Connection connection) {
        this.connection = connection;
    }

    public boolean isTableExist(String tableName) {
        try {
            return connection.getMetaData().getTables(connection.getCatalog(), null, tableName, null).next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void describeTable(String tableName) {
        try {
            ResultSet rs = connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, null);
            ResultSet table = connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, null);
            table.next();
            System.out.println(table.getString(3));
            while (rs.next()) {
                System.out.println("\t%s %s".formatted(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
