package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;
import za.ac.cput.service.INextOfKinContactService;
import za.ac.cput.service.IUserService;
import za.ac.cput.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/nextofkincontact")
public class NextOfKinContactController {

    private final INextOfKinContactService nextOfKinContactService;
    private final IUserService userService;

    @Autowired
    public NextOfKinContactController(INextOfKinContactService nextOfKinContactService, IUserService userService) {
        this.nextOfKinContactService = nextOfKinContactService;
        this.userService = userService;
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
        return nextOfKinContactService.findByFirstName(firstName);
    }

    @GetMapping("/findByLastName")
    public NextOfKinContact findByLastName(@RequestParam String lastName) {
        return nextOfKinContactService.findByLastName(lastName);
    }

    @GetMapping("/findByRelationship")
    public NextOfKinContact findByRelationship(@RequestParam String relationship) {
        return nextOfKinContactService.findByRelationship(relationship);
    }

    @GetMapping("/findByUser/{userId}")
    public List<NextOfKinContact> findByUser(@PathVariable String userId) {
        User user = userService.read(userId);
        return nextOfKinContactService.findByUser(user);
    }

    @GetMapping("/getAll")
    public List<NextOfKinContact> getAll() {
        return nextOfKinContactService.getAll();
    }

    @DeleteMapping("/delete/{nextOfKinContactId}")
    public boolean delete(@PathVariable String nextOfKinContactId) {
        return nextOfKinContactService.delete(nextOfKinContactId);
    }
}
