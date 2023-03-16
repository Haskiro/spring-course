package com.github.haskiro.dao;

import com.github.haskiro.models.Person;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PersonDAO {
    private final EntityManager entityManager;

    @Autowired
    public PersonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public void testNPlus1() {
        Session session = entityManager.unwrap(Session.class);

//        // 1 запрос
//        List<Person> people = session.createSelectionQuery("SELECT p FROM Person p", Person.class)
//                .getResultList();
//
//        // n запросов
//        for (Person person: people) {
//            System.out.println("Person " + person.getName() + "has: " + person.getItems());
//        }

        // Solution
        Set<Person> people = new HashSet<>(session.createSelectionQuery("SELECT p FROM Person p LEFT JOIN FETCH p.items", Person.class)
                .getResultList());

        for (Person person: people) {
            System.out.println("Person " + person.getName() + " has: " + person.getItems());
        }
    }
}
