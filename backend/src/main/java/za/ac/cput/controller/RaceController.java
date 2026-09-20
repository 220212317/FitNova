package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Race;
import za.ac.cput.service.IRaceService;

import java.util.List;

/*
 * RaceController.java
 * Author: Inga Plati
 * 230126634
 */

@RestController
@RequestMapping("/race")
public class RaceController {

    private final IRaceService raceService;

    @Autowired
    public RaceController(IRaceService raceService) {
        this.raceService = raceService;
    }

    @PostMapping("/create")
    public ResponseEntity<Race> create(@RequestBody Race race) {
        Race created = raceService.create(race);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{raceId}")
    public ResponseEntity<Race> read(@PathVariable String raceId) {
        Race race = raceService.read(raceId);
        if (race == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(race);
    }

    @PutMapping("/update")
    public ResponseEntity<Race> update(@RequestBody Race race) {
        Race updated = raceService.update(race);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{raceId}")
    public ResponseEntity<Void> delete(@PathVariable String raceId) {
        boolean deleted = raceService.delete(raceId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Race>> getAll() {
        List<Race> races = raceService.getAll();
        return ResponseEntity.ok(races);
    }
}