package com.tutrit.sqlinjection.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseUtilTest {
    @Autowired
    DatabaseUtil databaseUtil;

    @Test
    void isTableExist() {
        assertTrue(databaseUtil.isTableExist("PERSON"));
        assertFalse(databaseUtil.isTableExist("person"));
    }
}