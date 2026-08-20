package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.NextOfKinContact;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface INextOfKinContactRepository extends JpaRepository<NextOfKinContact, String> {
    Optional<NextOfKinContact> findByfirstName(String firstName);
    Optional<NextOfKinContact> findBylastName(String lastName);
    Optional<NextOfKinContact> findByrelationship(String relationship);
    Optional<NextOfKinContact> findBycellphoneNumber(String cellphoneNumber);
}
