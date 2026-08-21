/*
 * DemographicController.java
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
import za.ac.cput.domain.Demographic;
import za.ac.cput.service.IDemographicService;

import java.util.List;

@RestController
@RequestMapping("/demographic")
public class DemographicController {

    private final IDemographicService service;

    @Autowired
    public DemographicController(IDemographicService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Demographic create(@RequestBody Demographic demographic) {
        return service.create(demographic);
    }

    @GetMapping("/read/{demographyId}")
    public Demographic read(@PathVariable String demographyId) {
        return service.read(demographyId);
    }

    @PutMapping("/update")
    public Demographic update(@RequestBody Demographic demographic) {
        return service.update(demographic);
    }

    @DeleteMapping("/delete/{demographyId}")
    public void delete(@PathVariable String demographyId) {
        service.delete(demographyId);
    }

    @GetMapping("/getAll")
    public List<Demographic> getAll() {
        return service.getAll();
    }
}