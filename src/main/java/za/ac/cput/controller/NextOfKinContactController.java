package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.service.INextOfKinContactService;

import java.util.List;

@RestController
@RequestMapping("/nextofkincontact")
public class NextOfKinContactController {

    private final INextOfKinContactService nextOfKinContactService;

    @Autowired
    public NextOfKinContactController(INextOfKinContactService nextOfKinContactService) {
        this.nextOfKinContactService = nextOfKinContactService;
    }

    @PostMapping("/create")
    public NextOfKinContact create(@RequestBody NextOfKinContact nextOfKinContact) {
        return nextOfKinContactService.create(nextOfKinContact);
    }

    @GetMapping("/read/{nextOfKinContactId}")
    public NextOfKinContact read(@PathVariable String nextOfKinContactId) {
        return nextOfKinContactService.read(nextOfKinContactId);
    }

    @PutMapping("/update")
    public NextOfKinContact update(@RequestBody NextOfKinContact nextOfKinContact) {
        return nextOfKinContactService.update(nextOfKinContact);
    }

    @GetMapping("/findByFirstName")
    public NextOfKinContact findByFirstName(@RequestParam String firstName) {
        return nextOfKinContactService.findByfirstName(firstName);
    }

    @GetMapping("/findByLastName")
    public NextOfKinContact findByLastName(@RequestParam String lastName) {
        return nextOfKinContactService.findBylastName(lastName);
    }

    @GetMapping("/findByRelationship")
    public NextOfKinContact findByRelationship(@RequestParam String relationship) {
        return nextOfKinContactService.findByrelationship(relationship);
    }

    /*@GetMapping("/findByUser/{userId}")
    public List<NextOfKinContact> findByUserId(@PathVariable String userId) {
        return nextOfKinContactService.findByUser_UserId(userId);
    }*/

    @GetMapping("/getAll")
    public List<NextOfKinContact> getAll() {
        return nextOfKinContactService.getAll();
    }

    @DeleteMapping("/delete/{nextOfKinContactId}")
    public boolean delete(@PathVariable String nextOfKinContactId) {
        return nextOfKinContactService.delete(nextOfKinContactId);
    }
}
