package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.NextOfKinContact;

public interface INextOfKinContactRepository extends JpaRepository<NextOfKinContact, String> {
}
