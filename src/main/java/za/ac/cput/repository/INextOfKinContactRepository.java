package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.NextOfKinContact;

import java.util.List;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface INextOfKinContactRepository extends JpaRepository<NextOfKinContact, String> {
    NextOfKinContact findByfirstName(String firstName);
    NextOfKinContact findBylastName(String lastName);
    NextOfKinContact findByrelationship(String relationship);
    //List<NextOfKinContact> findByUser_UserId(String userId);
}
