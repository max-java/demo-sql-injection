package com.tutrit.sqlinjection.service;

import com.tutrit.sqlinjection.utils.DatabaseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateTableUseCaseTest {
    @Autowired
    CreateTableUseCase createTableUseCase;
    @Autowired DatabaseUtil databaseUtil;

    @Test
    void createTable() {
        String table = "MY_TABLE";
        assertFalse(databaseUtil.isTableExist(table));
        createTableUseCase.createTable(table, Map.of(
                "id", "int",
                "name", "varchar(8)",
                "secret", "varchar(5)"));
        assertTrue(databaseUtil.isTableExist(table));
        databaseUtil.describeTable(table);
    }

    @Test
    void createTable_InjectDrop() {
        assertTrue(databaseUtil.isTableExist("PERSON"));
        String table = "MY_TABLE; drop table `PERSON`;---";
        createTableUseCase.createTable(table, Map.of(
                "id", "int",
                "name", "varchar(8)",
                "secret", "varchar(5)"));
        assertFalse(databaseUtil.isTableExist("PERSON"));
    }

    @Test
    void createTable_InjectDrop_withPreparedStatement() {
        assertTrue(databaseUtil.isTableExist("PERSON"));
        String table = "MY_TABLE; drop table `PERSON`;---";
        String query = createTableUseCase.createQuery(table, Map.of(
                "id", "int",
                "name", "varchar(8)",
                "secret", "varchar(5)"));
        createTableUseCase.executeWithPreparedStatement(query);
        assertFalse(databaseUtil.isTableExist("PERSON"));
    }

    @Test
    void createTable_InjectDrop_withSanitizer() {
        assertTrue(databaseUtil.isTableExist("PERSON"));
        String table = "MY_TABLE; drop table `PERSON`;---";
        String query = createTableUseCase.createQuery(table, Map.of(
                "id", "int",
                "name", "varchar(8)",
                "secret", "varchar(5)"));
        createTableUseCase.sanitize(query);
        createTableUseCase.execute(query);
        assertTrue(databaseUtil.isTableExist("PERSON"));
    }
}
