package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.User;
import za.ac.cput.service.IUserService;

import java.util.List;

/*
 * UserController.java
 * Author: Collins Shibambo
 * 230093183
 */

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        User created = userService.create(user);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{userId}")
    public ResponseEntity<User> read(@PathVariable String userId) {
        User user = userService.read(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        User updated = userService.update(user);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        boolean deleted = userService.delete(userId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByName/{firstName}/{lastName}")
    public ResponseEntity<List<User>> findByFirstNameAndLastName(@PathVariable String firstName,
                                                                 @PathVariable String lastName) {
        List<User> users = userService.findByFirstNameAndLastName(firstName, lastName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/searchByLastName/{lastName}")
    public ResponseEntity<List<User>> searchByLastName(@PathVariable String lastName) {
        List<User> users = userService.searchByLastName(lastName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }
}