package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Account;
import za.ac.cput.service.IAccountService;

import java.time.LocalDate;
import java.util.List;

/*
 * AccountController.java
 * Author: Athi Sintiya
 * 220212317
 */

@RestController
@RequestMapping("/account")
public class AccountController {

    private final IAccountService accountService;

    @Autowired
    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> create(@RequestBody Account account) {
        Account created = accountService.create(account);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{accountId}")
    public ResponseEntity<Account> read(@PathVariable String accountId) {
        Account account = accountService.read(accountId);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PutMapping("/update")
    public ResponseEntity<Account> update(@RequestBody Account account) {
        Account updated = accountService.update(account);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Void> delete(@PathVariable String accountId) {
        boolean deleted = accountService.delete(accountId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<Account> findByEmail(@PathVariable String email) {
        Account account = accountService.findByEmail(email);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping("/findByRegistrationDate/{registrationDate}")
    public ResponseEntity<List<Account>> findAccountByRegistrationDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registrationDate) {
        List<Account> accounts = accountService.findAccountByRegistrationDate(registrationDate);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> accounts = accountService.getAll();
        return ResponseEntity.ok(accounts);
    }
}