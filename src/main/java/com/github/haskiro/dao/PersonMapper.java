package com.github.haskiro.dao;

import com.github.haskiro.models.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// Для представления строк из бд в виде объекта класса Person
public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getInt("id"));
        person.setName(rs.getString("name"));
        person.setEmail(rs.getString("email"));
        person.setAge(rs .getInt("age"));

        return person;
    }
}
