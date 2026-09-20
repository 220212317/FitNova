package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;

import java.util.List;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface INextOfKinContactRepository extends JpaRepository<NextOfKinContact, String> {
    NextOfKinContact findByFirstName(String firstName);
    NextOfKinContact findByLastName(String lastName);
    NextOfKinContact findByRelationship(String relationship);
    List<NextOfKinContact> findByUser(User user);
}
