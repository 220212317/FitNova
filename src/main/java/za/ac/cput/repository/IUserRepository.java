/*
 * IUserRepository.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.User;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    List<User> findByLastNameContainingIgnoreCase(String lastName);
}