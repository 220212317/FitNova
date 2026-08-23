package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Gender;
import za.ac.cput.service.IGenderService;

import java.util.List;

/*
 * GenderController.java
 * Author: Inga Plati
 * 230126634
 */

@RestController
@RequestMapping("/gender")
public class GenderController {

    private final IGenderService genderService;

    @Autowired
    public GenderController(IGenderService genderService) {
        this.genderService = genderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Gender> create(@RequestBody Gender gender) {
        Gender created = genderService.create(gender);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{genderId}")
    public ResponseEntity<Gender> read(@PathVariable String genderId) {
        Gender gender = genderService.read(genderId);
        if (gender == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gender);
    }

    @PutMapping("/update")
    public ResponseEntity<Gender> update(@RequestBody Gender gender) {
        Gender updated = genderService.update(gender);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{genderId}")
    public ResponseEntity<Void> delete(@PathVariable String genderId) {
        boolean deleted = genderService.delete(genderId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Gender>> getAll() {
        List<Gender> genders = genderService.getAll();
        return ResponseEntity.ok(genders);
    }
}