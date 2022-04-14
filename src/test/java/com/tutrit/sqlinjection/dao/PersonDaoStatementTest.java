package com.tutrit.sqlinjection.dao;

import com.tutrit.sqlinjection.bean.Person;
import org.h2.jdbc.JdbcSQLDataException;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonDaoStatementTest {
    @Autowired
    PersonDaoStatement dao;

    @Test
    void findById() throws SQLException {
        List<Person> personList = dao.findById("1");
        assertEquals(1, personList.size());
    }

    @Test
    void findById_Inject_AlwaysTrue() throws SQLException {
        List<Person> personList = dao.findById("1 or 1=1");
        assertEquals(3, personList.size());
    }

    @Test
    void findById_Inject_OrTrue() throws SQLException {
        List<Person> personList = dao.findById("1 or true");
        assertEquals(3, personList.size());
    }

    @Test
    void findById_Inject_True() throws SQLException {
        List<Person> personList = dao.findById("true");
        assertEquals(1, personList.size());
        assertEquals(new Person(1, "mikas", "MB003"), personList.get(0));
    }

    @Test
    void findByName() throws SQLException {
        List<Person> personList = dao.findByName("mikas");
        assertEquals(1, personList.size());
    }

    @Test
    void findByNameEqualsAlwaysTrue() throws SQLException {
        List<Person> personList = dao.findByName("' or ''='");
        assertEquals(3, personList.size());
    }

    @Test
    void findByNameBatchSql() {
        var thrown = assertThrows(
                JdbcSQLSyntaxErrorException.class,
                () -> dao.findByName("mikas'; drop table `Person`; select * from person where name = 'mikas"));

        assertTrue(thrown.getMessage().contains("Table \"PERSON\" not found"));
        assertEquals("Table \"PERSON\" not found; SQL statement:\n" +
                " select * from person where name = 'mikas' [42102-200]", thrown.getMessage());
    }
}