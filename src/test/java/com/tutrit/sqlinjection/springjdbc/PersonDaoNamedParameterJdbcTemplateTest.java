package com.tutrit.sqlinjection.springjdbc;

import com.tutrit.sqlinjection.bean.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonDaoNamedParameterJdbcTemplateTest {
    @Autowired
    PersonDaoNamedParameterJdbcTemplate dao;

    @Test
    void findById() {
        List<Person> personList = dao.findById("1");
        assertEquals(1, personList.size());
    }

    @Test
    void findById_Inject_AlwaysTrue() {
        var thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> dao.findById("1 or 1=1"));
        assertEquals("PreparedStatementCallback; SQL [select * from person where id = ?]; Data conversion error converting \"1 or 1=1\"; SQL statement:\n" +
                "select * from person where id = ? [22018-200]; nested exception is org.h2.jdbc.JdbcSQLDataException: Data conversion error converting \"1 or 1=1\"; SQL statement:\n" +
                "select * from person where id = ? [22018-200]", thrown.getMessage());
    }

    @Test
    void findById_Inject_OrTrue() {
        var thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> dao.findById("1 or true"));
        assertEquals("PreparedStatementCallback; SQL [select * from person where id = ?]; Data conversion error converting \"1 or true\"; SQL statement:\n" +
                "select * from person where id = ? [22018-200]; nested exception is org.h2.jdbc.JdbcSQLDataException: Data conversion error converting \"1 or true\"; SQL statement:\n" +
                "select * from person where id = ? [22018-200]", thrown.getMessage());
    }
}