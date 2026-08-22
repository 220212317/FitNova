package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.service.IUserRoleService;

import java.util.List;
import java.util.Optional;

/*
 * UserRoleController.java
 * Author: Collins Shibambo
 * 230093183
 */

@RestController
@RequestMapping("/userrole")
public class UserRoleController {

    private final IUserRoleService userRoleService;

    @Autowired
    public UserRoleController(IUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserRole> create(@RequestBody UserRole userRole) {
        UserRole created = userRoleService.create(userRole);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{userRoleId}")
    public ResponseEntity<UserRole> read(@PathVariable String userRoleId) {
        UserRole userRole = userRoleService.read(userRoleId);
        if (userRole == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRole);
    }

    @PutMapping("/update")
    public ResponseEntity<UserRole> update(@RequestBody UserRole userRole) {
        UserRole updated = userRoleService.update(userRole);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{userRoleId}")
    public ResponseEntity<Void> delete(@PathVariable String userRoleId) {
        boolean deleted = userRoleService.delete(userRoleId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByUser/{userId}")
    public ResponseEntity<List<UserRole>> findByUser(@PathVariable String userId) {
        List<UserRole> userRoles = userRoleService.findByUser(userId);
        return ResponseEntity.ok(userRoles);
    }

    @GetMapping("/findByRole/{roleId}")
    public ResponseEntity<List<UserRole>> findByRole(@PathVariable RoleType roleId) {
        List<UserRole> userRoles = userRoleService.findByRole(roleId);
        return ResponseEntity.ok(userRoles);
    }

    @GetMapping("/findByUserAndRole/{userId}/{roleId}")
    public ResponseEntity<UserRole> findByUserAndRole(@PathVariable String userId,
                                                      @PathVariable RoleType roleId) {
        Optional<UserRole> userRole = userRoleService.findByUserAndRole(userId, roleId);
        return userRole.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserRole>> getAll() {
        List<UserRole> userRoles = userRoleService.getAll();
        return ResponseEntity.ok(userRoles);
    }
}