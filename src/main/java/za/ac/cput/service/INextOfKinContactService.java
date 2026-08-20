package za.ac.cput.service;
import za.ac.cput.domain.NextOfKinContact;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface INextOfKinContactService  extends IService<NextOfKinContact,String>{
    Optional<NextOfKinContact> findByfirstName(String firstName);
    Optional<NextOfKinContact> findBylastName(String lastName);
    Optional<NextOfKinContact> findByrelationship(String relationship);
    Optional<NextOfKinContact> findBycellphoneNumber(String cellphoneNumber);
}
