package com.tutrit.sqlinjection.dao;

import com.tutrit.sqlinjection.bean.Person;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.tutrit.sqlinjection.dao.PersonMapper.toPerson;

@Component
public class PersonDaoPreparedStatement {
    final Connection connection;

    public PersonDaoPreparedStatement(final Connection connection) {
        this.connection = connection;
    }

    public List<Person> findById(String id) throws SQLException {
        List<Person> personList = new ArrayList<>();
        PreparedStatement st = connection.prepareStatement("select * from person where id = ?");
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            personList.add(toPerson(rs));
        }
        return personList;
    }

    public List<Person> findByName(String name) throws SQLException {
        List<Person> personList = new ArrayList<>();
        PreparedStatement st = connection.prepareStatement("select * from person where name = ?");
        st.setString(1, name);
        LoggerFactory.getLogger("QUERY TO EXECUTE").info(st.toString());
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            personList.add(toPerson(rs));
        }
        return personList;
    }
}
