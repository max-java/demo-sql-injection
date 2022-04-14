package com.tutrit.sqlinjection.springjdbc;

import com.tutrit.sqlinjection.bean.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDaoJdbcTemplate {
    final JdbcTemplate jdbcTemplate;

    public PersonDaoJdbcTemplate(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> findById(String id) {
        String query = "select * from person where id = " + id;
        return jdbcTemplate.query(query, new BeanPropertyRowMapper(Person.class));
    }

    public List<Person> findByName(String name) {
        String query = "select * from person where name = '" + name + "'";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper(Person.class));
    }
}
