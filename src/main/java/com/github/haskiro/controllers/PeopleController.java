package com.github.haskiro.controllers;

import com.github.haskiro.dao.PersonDAO;
import com.github.haskiro.models.Person;
import com.github.haskiro.services.ItemsService;
import com.github.haskiro.services.PeopleService;
import com.github.haskiro.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ItemsService itemsService;
    private final PersonValidator personValidator;

    private final PersonDAO personDAO;
    @Autowired
    public PeopleController(PeopleService peopleService, ItemsService itemsService, PersonValidator personValidator, PersonDAO personDAO) {
        this.peopleService = peopleService;
        this.itemsService = itemsService;
        this.personValidator = personValidator;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());

//        itemsService.findByItemName("Airpods");
//        itemsService.findByOwner(peopleService.findAll().get(1));

//        peopleService.test();

        personDAO.testNPlus1();

        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        // Получим одного человека по его id из dao и передадим на отображение в представление
        model.addAttribute("person", peopleService.findOne(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()){
            return "people/edit";
        }

        peopleService.update(id, person);
        return "redirect:/people/" + id;
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        peopleService.save(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);

        return "redirect:/people";
    }
}
