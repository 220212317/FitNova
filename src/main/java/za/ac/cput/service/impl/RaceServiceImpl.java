/*
 * RaceServiceImpl.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Race;
import za.ac.cput.repository.IRaceRepository;
import za.ac.cput.service.IRaceService;

import java.util.List;

@Service
public class RaceServiceImpl implements IRaceService {

    private final IRaceRepository repository;

    @Autowired
    public RaceServiceImpl(IRaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Race create(Race race) {
        return repository.save(race);
    }

    @Override
    public Race read(String raceId) {
        return repository.findById(raceId).orElse(null);
    }

    @Override
    public Race update(Race race) {
        if (!repository.existsById(race.getRaceId())) {
            return null;
        }
        return repository.save(race);
    }

    @Override
    public void delete(String raceId) {
        repository.deleteById(raceId);
    }

    @Override
    public List<Race> getAll() {
        return repository.findAll();
    }
}