/*
 * IGenderService.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service;

import za.ac.cput.domain.Gender;

import java.util.List;

public interface IGenderService {

    Gender create(Gender gender);

    Gender read(String genderId);

    Gender update(Gender gender);

    void delete(String genderId);

    List<Gender> getAll();
}