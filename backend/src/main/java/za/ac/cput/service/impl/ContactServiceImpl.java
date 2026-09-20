package za.ac.cput.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Contact;
import za.ac.cput.repository.IContactRepository;
import za.ac.cput.service.IContactService;
import java.util.List;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

@Service
public class ContactServiceImpl implements IContactService {

    private IContactRepository repository;

    @Autowired
    public ContactServiceImpl(IContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Contact create(Contact contact) {
        return repository.save(contact);
    }

    @Override
    public Contact read(String contactId) {
        return repository.findById(contactId).orElse(null);
    }

    @Override
    public Contact update(Contact contact) {
        if(repository.existsById(contact.getContactId())){
            return repository.save(contact);
        }
        return null;
    }

    @Override
    public Contact findByCellphoneNumber(String cellphoneNumber) {
        return repository.findByCellphoneNumber(cellphoneNumber);
    }

    @Override
    public Contact findByEmailAddress(String emailAddress) {
        return repository.findByEmailAddress(emailAddress);
    }

    /*@Override
    public Contact findByUser_UserId(String userId) {
        return repository.findByUser_UserId(userId);
    }*/

    @Override
    public List<Contact> getAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(String contactId) {
        if(repository.existsById(contactId)){
            repository.deleteById(contactId);
            return true;
        }
        return false;

    }
}
