package com.tutrit.sqlinjection.dao;

import com.tutrit.sqlinjection.bean.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

class PersonMapper {
    static Person toPerson(ResultSet rs) throws SQLException {
        return new Person(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3));
    }
}
