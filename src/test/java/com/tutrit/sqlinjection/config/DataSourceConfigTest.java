package com.tutrit.sqlinjection.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DataSourceConfigTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    Connection connection;

    @Test
    void jdbcTemplate() {
        assertEquals(3, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM person", Integer.class));
    }

    @Test
    void namedParameterJdbcTemplate() {
        assertEquals(3, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM person", Integer.class));
    }

    @Test
    void connection() throws Exception {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM person");
        rs.next();
        assertEquals(3, rs.getInt(1));
    }
}