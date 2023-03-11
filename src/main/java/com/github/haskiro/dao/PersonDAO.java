package com.github.haskiro.dao;

import com.github.haskiro.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM person where email=?", new Object[]{email}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public Person show(int id) {
        //   sql запрос, по умолч использует preparedStatement,  массив с параметрам, row mapper
        // row mapper - для мапа данных, пришедших с БД в качестве person
        // row mapper - для мапа данных, пришедших с БД в качестве person
        return jdbcTemplate.query("SELECT * FROM person where id=?", new Object[]{id} , new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);

    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, age, email, address) VALUES(?, ?, ?, ?)", person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET name=?, age=?, email=?, address=? WHERE  id=?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getAddress(), id);
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE person SET name=?, age=?, email=? WHERE id=?;");
//            preparedStatement.setString(1, updatedPerson.getName());
//            preparedStatement.setInt(2, updatedPerson.getAge());
//            preparedStatement.setString(3, updatedPerson.getEmail());
//            preparedStatement.setInt(4, id);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person WHERE id=?;");
//            preparedStatement.setInt(1, id);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }


    //////////////////////////////////
    //// Тестируем производительность пакетной вставки
    /////////////////////////////////

    public void testMultipleUpdate() {
        List<Person> people = createThousandPeople();

        long before = System.currentTimeMillis();

        for(Person person: people) {
            jdbcTemplate.update("INSERT INTO person VALUES(?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    public void testBatchUpdate() {
        List<Person> people = createThousandPeople();

        long before = System.currentTimeMillis();

        // один запрос из 1000 вставок в бд, вместо 1000 запросов на вставку
        jdbcTemplate.batchUpdate("INSERT INTO person VALUES (?, ?, ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, people.get(i).getId());
                        ps.setString(2, people.get(i).getName());
                        ps.setInt(3, people.get(i).getAge());
                        ps.setString(4, people.get(i).getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    private List<Person> createThousandPeople() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name", 30, "test" + i + "@mail.ru", "some address"));
        }

        return people;
    }
}
