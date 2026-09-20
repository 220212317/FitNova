package za.ac.cput.service;
import za.ac.cput.domain.Contact;
//Lisakhanya Tshokolo 220239215

public interface IContactService extends IService<Contact,String> {
    Contact findByCellphoneNumber(String cellphoneNumber);
    Contact findByEmailAddress(String emailAddress);
}
