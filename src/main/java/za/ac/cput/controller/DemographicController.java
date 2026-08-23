package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Demographic;
import za.ac.cput.service.IDemographicService;

import java.util.List;

/*
 * DemographicController.java
 * Author: Inga Plati
 * 230126634
 */

@RestController
@RequestMapping("/demographic")
public class DemographicController {

    private final IDemographicService demographicService;

    @Autowired
    public DemographicController(IDemographicService demographicService) {
        this.demographicService = demographicService;
    }

    @PostMapping("/create")
    public ResponseEntity<Demographic> create(@RequestBody Demographic demographic) {
        Demographic created = demographicService.create(demographic);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{demographyId}")
    public ResponseEntity<Demographic> read(@PathVariable String demographyId) {
        Demographic demographic = demographicService.read(demographyId);
        if (demographic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(demographic);
    }

    @PutMapping("/update")
    public ResponseEntity<Demographic> update(@RequestBody Demographic demographic) {
        Demographic updated = demographicService.update(demographic);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{demographyId}")
    public ResponseEntity<Void> delete(@PathVariable String demographyId) {
        boolean deleted = demographicService.delete(demographyId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Demographic>> getAll() {
        List<Demographic> demographics = demographicService.getAll();
        return ResponseEntity.ok(demographics);
    }
}