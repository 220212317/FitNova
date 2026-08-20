package za.ac.cput.service;
import za.ac.cput.domain.NextOfKinContact;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface INextOfKinContactService  extends IService<NextOfKinContact,String>{
    NextOfKinContact findByfirstName(String firstName);
    NextOfKinContact findBylastName(String lastName);
    NextOfKinContact findByrelationship(String relationship);
}
