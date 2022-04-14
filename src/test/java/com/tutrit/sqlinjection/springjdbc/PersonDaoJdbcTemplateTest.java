package com.tutrit.sqlinjection.springjdbc;

import com.tutrit.sqlinjection.bean.Person;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonDaoJdbcTemplateTest {
    @Autowired
    PersonDaoJdbcTemplate dao;

    @Test
    void findById() {
        List<Person> personList = dao.findById("1");
        assertEquals(1, personList.size());
    }

    @Test
    void findById_Inject_AlwaysTrue() {
        List<Person> personList = dao.findById("1 or 1=1");
        assertEquals(3, personList.size());
    }

    @Test
    void findById_Inject_OrTrue() {
        List<Person> personList = dao.findById("1 or true");
        assertEquals(3, personList.size());
    }

    @Test
    void findById_Inject_True() {
        List<Person> personList = dao.findById("true");
        assertEquals(1, personList.size());
        assertEquals(new Person(), personList.get(0));
    }

    @Test
    void findByName() {
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
                BadSqlGrammarException.class,
                () -> dao.findByName("mikas'; drop table `Person`; select * from person where name = 'mikas"));

        assertTrue(thrown.getMessage().contains("Table \"PERSON\" not found"));
        assertEquals("StatementCallback; bad SQL grammar [select * from person where name = 'mikas'; drop table `Person`; select * from person where name = 'mikas']; nested exception is org.h2.jdbc.JdbcSQLSyntaxErrorException: Table \"PERSON\" not found; SQL statement:\n" +
                " select * from person where name = 'mikas' [42102-200]", thrown.getMessage());
    }
}