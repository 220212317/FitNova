/*
 * IRaceService.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service;

import za.ac.cput.domain.Race;

import java.util.List;

public interface IRaceService {

    Race create(Race race);

    Race read(String raceId);

    Race update(Race race);

    void delete(String raceId);

    List<Race> getAll();
}