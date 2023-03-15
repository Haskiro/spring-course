package com.github.haskiro.util;

import com.github.haskiro.models.Person;
import com.github.haskiro.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> personToBeUpdated = peopleService.findOneByEmail(person.getEmail());

        if (personToBeUpdated.isPresent() && personToBeUpdated.get().getId() != person.getId()) {
            errors.rejectValue("email", "", "This email is already taken");
        }
        // нужно посмотреть, если человек с таким же email в БД


    }
}
