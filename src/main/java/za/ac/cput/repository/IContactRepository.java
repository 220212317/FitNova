package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Contact;
import java.util.Optional;
//Lisakhanya Tshokolo 220239215

public interface IContactRepository extends JpaRepository<Contact, String> {
    Optional<Contact> findByCellphoneNumber(String cellphoneNumber);
    Optional<Contact> findByEmailAddress(String emailAddress);
}
