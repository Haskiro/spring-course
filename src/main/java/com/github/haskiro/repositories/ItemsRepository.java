package com.github.haskiro.repositories;

import com.github.haskiro.models.Item;
import com.github.haskiro.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Integer> {
    //person.getItems()
    List<Item> findByOwner(Person person);

    List<Item> findByItemName(String itemName);
}
