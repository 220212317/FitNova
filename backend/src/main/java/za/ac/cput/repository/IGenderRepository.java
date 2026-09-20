/*
 * IGenderRepository.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Gender;

public interface IGenderRepository extends JpaRepository<Gender, String> {
}