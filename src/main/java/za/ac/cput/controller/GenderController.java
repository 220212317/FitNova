/*
 * GenderController.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.domain.Gender;
import za.ac.cput.service.IGenderService;

import java.util.List;

@RestController
@RequestMapping("/gender")
public class GenderController {

    private final IGenderService service;

    @Autowired
    public GenderController(IGenderService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Gender create(@RequestBody Gender gender) {
        return service.create(gender);
    }

    @GetMapping("/read/{genderId}")
    public Gender read(@PathVariable String genderId) {
        return service.read(genderId);
    }

    @PutMapping("/update")
    public Gender update(@RequestBody Gender gender) {
        return service.update(gender);
    }

    @DeleteMapping("/delete/{genderId}")
    public void delete(@PathVariable String genderId) {
        service.delete(genderId);
    }

    @GetMapping("/getAll")
    public List<Gender> getAll() {
        return service.getAll();
    }
}