/*
 * RaceController.java
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
import za.ac.cput.domain.Race;
import za.ac.cput.service.IRaceService;

import java.util.List;

@RestController
@RequestMapping("/race")
public class RaceController {

    private final IRaceService service;

    @Autowired
    public RaceController(IRaceService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Race create(@RequestBody Race race) {
        return service.create(race);
    }

    @GetMapping("/read/{raceId}")
    public Race read(@PathVariable String raceId) {
        return service.read(raceId);
    }

    @PutMapping("/update")
    public Race update(@RequestBody Race race) {
        return service.update(race);
    }

    @DeleteMapping("/delete/{raceId}")
    public void delete(@PathVariable String raceId) {
        service.delete(raceId);
    }

    @GetMapping("/getAll")
    public List<Race> getAll() {
        return service.getAll();
    }
}