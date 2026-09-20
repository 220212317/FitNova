package za.ac.cput.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Contact;
import za.ac.cput.service.IContactService;
//Lisakhanya Tshokolo 220239215

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final IContactService contactService;

    @Autowired
    public ContactController(IContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/create")
    public Contact create(@RequestBody Contact contact) {
        return contactService.create(contact);
    }

    @GetMapping("/read/{contactId}")
    public Contact read(@PathVariable("contactId") String contactId) {
        return contactService.read(contactId);
    }

    @PutMapping("/update")
    public Contact update(@RequestBody Contact contact) {
        return contactService.update(contact);
    }

    @GetMapping("/findByCellphoneNumber")
    public Contact findByCellphoneNumber(@RequestParam String cellphoneNumber) {
        return contactService.findByCellphoneNumber(cellphoneNumber);
    }

    @GetMapping("/findByEmailAddress")
    public Contact findByEmailAddress(@RequestParam String emailAddress) {
        return contactService.findByEmailAddress(emailAddress);
    }

    @GetMapping("/getAll")
    public java.util.List<Contact> getAll() {
        return contactService.getAll();
    }

    @DeleteMapping("/delete/{contactId}")
    public boolean delete(@PathVariable String contactId) {
        return contactService.delete(contactId);
    }
}
