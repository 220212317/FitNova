package za.ac.cput.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;
import za.ac.cput.repository.INextOfKinContactRepository;
import za.ac.cput.service.INextOfKinContactService;
import java.util.List;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

@Service
public class NextOfKinContactServiceImpl  implements INextOfKinContactService {

    private final INextOfKinContactRepository repository;

    @Autowired
    public NextOfKinContactServiceImpl(INextOfKinContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public NextOfKinContact create(NextOfKinContact nextOfKinContact) {
        return repository.save(nextOfKinContact);
    }

    @Override
    public NextOfKinContact read(String nextOfKinContactId) {
        return repository.findById(nextOfKinContactId).orElse(null);
    }

    @Override
    public NextOfKinContact update(NextOfKinContact nextOfKinContact) {
        if(repository.existsById(nextOfKinContact.getNextOfKinContactId())){
            return repository.save(nextOfKinContact);
        }
        return null;
    }

    @Override
    public List<NextOfKinContact> getAll() {
        return repository.findAll();
    }

    @Override
    public NextOfKinContact findByFirstName(String firstName) {
        return repository.findByFirstName(firstName);
    }

    @Override
    public NextOfKinContact findByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    @Override
    public NextOfKinContact findByRelationship(String relationship) {
        return repository.findByRelationship(relationship);
    }

    @Override
    public List<NextOfKinContact> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public boolean delete(String nextOfKinContactId) {
        if(repository.existsById(nextOfKinContactId)){
            repository.deleteById(nextOfKinContactId);
            return true;
        }
        return false;
    }
}
