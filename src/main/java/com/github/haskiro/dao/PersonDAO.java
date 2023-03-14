package com.github.haskiro.dao;

import com.github.haskiro.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();
        List<Person> people = session.createSelectionQuery("Select p FROM Person p", Person.class).getResultList();
        return people;
    }

    public Optional<Person> show(String email) {
        return null;
    }

    public Person show(int id) {
        return null;

    }

    public void save(Person person) {


    }

    public void update(int id, Person updatedPerson) {

    }

    public void delete(int id) {


    }


    //////////////////////////////////
    //// Тестируем производительность пакетной вставки
    /////////////////////////////////

    public void testMultipleUpdate() {
        List<Person> people = createThousandPeople();

        long before = System.currentTimeMillis();

        for(Person person: people) {
//            jdbcTemplate.update("INSERT INTO person VALUES(?, ?, ?, ?)",
//                    person.getId(), person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    public void testBatchUpdate() {
        List<Person> people = createThousandPeople();

        long before = System.currentTimeMillis();

        // один запрос из 1000 вставок в бд, вместо 1000 запросов на вставку
//        jdbcTemplate.batchUpdate("INSERT INTO person VALUES (?, ?, ?, ?);",
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setInt(1, people.get(i).getId());
//                        ps.setString(2, people.get(i).getName());
//                        ps.setInt(3, people.get(i).getAge());
//                        ps.setString(4, people.get(i).getEmail());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return people.size();
//                    }
//                });

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    private List<Person> createThousandPeople() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person("Name", 30, "test" + i + "@mail.ru", "some address"));
        }

        return people;
    }
}
