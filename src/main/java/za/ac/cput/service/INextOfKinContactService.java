package za.ac.cput.service;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;

import java.util.List;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface INextOfKinContactService  extends IService<NextOfKinContact,String>{
    NextOfKinContact findByFirstName(String firstName);
    NextOfKinContact findByLastName(String lastName);
    NextOfKinContact findByRelationship(String relationship);
    List<NextOfKinContact> findByUser(User user);
}
