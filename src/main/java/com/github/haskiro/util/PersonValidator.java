package com.github.haskiro.util;

import com.github.haskiro.dao.PersonDAO;
import com.github.haskiro.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> personToBeUpdated = personDAO.show(person.getEmail());

        if (personToBeUpdated.isPresent() && personToBeUpdated.get().getId() != person.getId()) {
            errors.rejectValue("email", "", "This email is already taken");
        }
        // нужно посмотреть, если человек с таким же email в БД


    }
}
