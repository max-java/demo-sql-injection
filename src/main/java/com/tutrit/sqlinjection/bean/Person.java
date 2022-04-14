package com.tutrit.sqlinjection.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Person {
    @Id
    @GeneratedValue
    Integer id;
    String name;
    String secret;

    public Person() {
    }

    public Person(final Integer id, final String name, final String secret) {
        this.id = id;
        this.name = name;
        this.secret = secret;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(secret, person.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, secret);
    }
}
