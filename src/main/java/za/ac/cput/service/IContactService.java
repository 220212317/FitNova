package za.ac.cput.service;
import za.ac.cput.domain.Contact;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface IContactService extends IService<Contact,String> {
    Optional<Contact> findByCellphoneNumber(String cellphoneNumber);
    Optional<Contact> findByEmailAddress(String emailAddress);
}
