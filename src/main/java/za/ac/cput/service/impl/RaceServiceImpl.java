package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Race;
import za.ac.cput.repository.IRaceRepository;
import za.ac.cput.service.IRaceService;
import za.ac.cput.util.Helper;

import java.util.List;

/*
 * Author: Inga Plati
 * 230126634
 */

@Service
public class RaceServiceImpl implements IRaceService {

    private final IRaceRepository repository;

    @Autowired
    public RaceServiceImpl(IRaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Race create(Race race) {
        if (race == null) {
            return null;
        }
        if (Helper.isNullOrEmpty(race.getDescription())) {
            throw new IllegalArgumentException("Race description is required.");
        }
        String id = race.getRaceId();
        if (Helper.isNullOrEmpty(id)) {
            race = new Race.Builder()
                    .copy(race)
                    .setRaceId(Helper.generateId())
                    .build();
        }
        return repository.save(race);
    }

    @Override
    public Race read(String raceId) {
        if (raceId == null) {
            return null;
        }
        return repository.findById(raceId).orElse(null);
    }

    @Override
    public Race update(Race race) {
        if (race == null || Helper.isNullOrEmpty(race.getRaceId())) {
            return null;
        }
        if (!repository.existsById(race.getRaceId())) {
            return null;
        }
        if (Helper.isNullOrEmpty(race.getDescription())) {
            throw new IllegalArgumentException("Race description is required.");
        }
        return repository.save(race);
    }

    @Override
    public boolean delete(String raceId) {
        if (raceId == null || !repository.existsById(raceId)) {
            return false;
        }
        try {
            repository.deleteById(raceId);
            return true;
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "Cannot delete this race because it is still used by one or more demographic records. " +
                            "Remove or reassign those profiles first.");
        }
    }

    @Override
    public List<Race> getAll() {
        return repository.findAll();
    }
}