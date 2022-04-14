package com.tutrit.sqlinjection.dao;

import com.tutrit.sqlinjection.bean.Person;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.tutrit.sqlinjection.dao.PersonMapper.toPerson;

@Component
public class PersonDaoStatement {
    final Connection connection;

    public PersonDaoStatement(final Connection connection) {
        this.connection = connection;
    }

    public List<Person> findById(String id) throws SQLException {
        List<Person> personList = new ArrayList<>();
        Statement st = connection.createStatement();
        String query = "select * from person where id = " + id;
        LoggerFactory.getLogger("QUERY TO EXECUTE").info(query);
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            personList.add(toPerson(rs));
        }
        return personList;
    }

    public List<Person> findByName(String name) throws SQLException {
        List<Person> personList = new ArrayList<>();
        Statement st = connection.createStatement();
        String query = "select * from person where name = '" + name + "'";
        LoggerFactory.getLogger("QUERY TO EXECUTE").info(query);
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            personList.add(toPerson(rs));
        }
        return personList;
    }
}
