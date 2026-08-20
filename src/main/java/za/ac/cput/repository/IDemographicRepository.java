/*
 * IDemographicRepository.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Demographic;

public interface IDemographicRepository extends JpaRepository<Demographic, String> {
}