package za.ac.cput.service.impl;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.repository.INextOfKinContactRepository;
import za.ac.cput.service.INextOfKinContactService;
import java.util.List;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

@Service
public class NextOfKinContactServiceImpl  implements INextOfKinContactService {

    private INextOfKinContactRepository repository;

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
    public Optional<NextOfKinContact> findByfirstName(String firstName) {
        return repository.findByfirstName(firstName);
    }

    @Override
    public Optional<NextOfKinContact> findBylastName(String lastName) {
        return repository.findBylastName(lastName);
    }

    @Override
    public Optional<NextOfKinContact> findByrelationship(String relationship) {
        return repository.findByrelationship(relationship);
    }

    @Override
    public Optional<NextOfKinContact> findBycellphoneNumber(String cellphoneNumber) {
        return repository.findBycellphoneNumber(cellphoneNumber);
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
