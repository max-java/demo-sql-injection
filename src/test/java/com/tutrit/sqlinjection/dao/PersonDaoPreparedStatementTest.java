package com.tutrit.sqlinjection.dao;

import com.tutrit.sqlinjection.bean.Person;
import org.h2.jdbc.JdbcSQLDataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PersonDaoPreparedStatementTest {
    @Autowired
    PersonDaoPreparedStatement dao;

    @Test
    void findById() throws SQLException {
        List<Person> personList = dao.findById("1");
        assertEquals(1, personList.size());
    }

    @Test
    void findById_Inject_AlwaysTrue() {
        var thrown = assertThrows(
                JdbcSQLDataException.class,
                () -> dao.findById("1 or 1=1"));
        assertEquals("Data conversion error converting \"1 or 1=1\"; SQL statement:\n" +
                "select * from person where id = ? [22018-200]", thrown.getMessage());
    }

    @Test
    void findById_Inject_OrTrue() {
        var thrown = assertThrows(
                JdbcSQLDataException.class,
                () -> dao.findById("1 or true"));
        assertEquals("Data conversion error converting \"1 or true\"; SQL statement:\n" +
                "select * from person where id = ? [22018-200]", thrown.getMessage());
    }

    @Test
    void findById_Inject_True() {
        var thrown = assertThrows(
                JdbcSQLDataException.class,
                () -> dao.findById("true"));
        assertEquals("Data conversion error converting \"true\"; SQL statement:\n" +
                "select * from person where id = ? [22018-200]", thrown.getMessage());
    }

    @Test
    void findByName() throws SQLException {
        List<Person> personList = dao.findByName("mikas");
        assertEquals(1, personList.size());
    }

    @Test
    void findByNameEqualsAlwaysTrue() throws SQLException {
        assertEquals(0, dao.findByName("' or ''='").size());
        assertEquals(0, dao.findByName("' or '='").size());
        assertEquals(0, dao.findByName(" or =").size());
    }

    @Test
    void findByNameBatchSql() throws SQLException {
        assertEquals(0, dao.findByName("mikas'; drop table `Person`; select * from person where name = 'mikas").size());
        assertEquals(0, dao.findByName("mikas'\"; drop table `Person`; select * from person where name = 'mikas").size());
        assertEquals(1, dao.findByName("mikas").size());
    }
}
