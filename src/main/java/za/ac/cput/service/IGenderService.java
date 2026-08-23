package za.ac.cput.service;

import za.ac.cput.domain.Gender;

import java.util.List;

/*
 * Author: Inga Plati
 * 230126634
 */

public interface IGenderService {

    Gender create(Gender gender);

    Gender read(String genderId);

    Gender update(Gender gender);

    boolean delete(String genderId);

    List<Gender> getAll();
}