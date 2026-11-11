package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Race;
import za.ac.cput.repository.IRaceRepository;
import za.ac.cput.service.IRaceService;
import za.ac.cput.util.Helper;
import za.ac.cput.util.LookupNames;

import java.util.List;
import java.util.Objects;

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
    public synchronized Race create(Race race) {
        if (race == null) {
            return null;
        }
        String description = LookupNames.clean(race.getDescription());
        if (LookupNames.key(description).isEmpty()) {
            throw new IllegalArgumentException("Race description is required.");
        }
        assertNotDuplicate(description, race.getRaceId());
        String id = race.getRaceId();
        if (Helper.isNullOrEmpty(id)) {
            id = Helper.generateId();
        }
        return repository.save(new Race.Builder()
                .copy(race)
                .setRaceId(id)
                .setDescription(description)
                .build());
    }

    @Override
    public Race read(String raceId) {
        if (raceId == null) {
            return null;
        }
        return repository.findById(raceId).orElse(null);
    }

    @Override
    public synchronized Race update(Race race) {
        if (race == null || Helper.isNullOrEmpty(race.getRaceId())) {
            return null;
        }
        if (!repository.existsById(race.getRaceId())) {
            return null;
        }
        String description = LookupNames.clean(race.getDescription());
        if (LookupNames.key(description).isEmpty()) {
            throw new IllegalArgumentException("Race description is required.");
        }
        assertNotDuplicate(description, race.getRaceId());
        return repository.save(new Race.Builder()
                .copy(race)
                .setDescription(description)
                .build());
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

    private void assertNotDuplicate(String description, String ownId) {
        String key = LookupNames.key(description);
        for (Race existing : repository.findAll()) {
            if (Objects.equals(existing.getRaceId(), ownId)) {
                continue;
            }
            if (key.equals(LookupNames.key(existing.getDescription()))) {
                throw new IllegalStateException(
                        "A race called \"" + existing.getDescription() + "\" already exists.");
            }
        }
    }
}