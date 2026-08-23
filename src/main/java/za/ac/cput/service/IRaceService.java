package za.ac.cput.service;

import za.ac.cput.domain.Race;

import java.util.List;

/*
 * Author: Inga Plati
 * 230126634
 */

public interface IRaceService {

    Race create(Race race);

    Race read(String raceId);

    Race update(Race race);

    boolean delete(String raceId);

    List<Race> getAll();
}