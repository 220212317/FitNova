/*
 * DemographicServiceImpl.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Race;
import za.ac.cput.repository.IDemographicRepository;
import za.ac.cput.service.IDemographicService;
import za.ac.cput.util.Helper;

import java.util.List;
import java.util.Objects;

@Service
public class DemographicServiceImpl implements IDemographicService {

    private final IDemographicRepository repository;

    @Autowired
    public DemographicServiceImpl(IDemographicRepository repository) {
        this.repository = repository;
    }

    @Override
    public synchronized Demographic create(Demographic demographic) {
        if (demographic == null) {
            return null;
        }
        if (demographic.getGender() == null && demographic.getRace() == null) {
            throw new IllegalArgumentException("A demographic needs a gender, a race, or both.");
        }
        assertNotDuplicate(demographic, demographic.getDemographyId());
        String id = demographic.getDemographyId();
        if (Helper.isNullOrEmpty(id)) {
            demographic = new Demographic.Builder()
                    .copy(demographic)
                    .setDemographyId(Helper.generateId())
                    .build();
        }
        return repository.save(demographic);
    }

    @Override
    public Demographic read(String demographyId) {
        if (demographyId == null) {
            return null;
        }
        return repository.findById(demographyId).orElse(null);
    }

    @Override
    public synchronized Demographic update(Demographic demographic) {
        if (demographic == null || Helper.isNullOrEmpty(demographic.getDemographyId())) {
            return null;
        }
        if (!repository.existsById(demographic.getDemographyId())) {
            return null;
        }
        if (demographic.getGender() == null && demographic.getRace() == null) {
            throw new IllegalArgumentException("A demographic needs a gender, a race, or both.");
        }
        assertNotDuplicate(demographic, demographic.getDemographyId());
        return repository.save(demographic);
    }

    @Override
    public boolean delete(String demographyId) {
        if (demographyId == null || !repository.existsById(demographyId)) {
            return false;
        }
        try {
            repository.deleteById(demographyId);
            return true;
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "Cannot delete this demographic because a member profile still uses it. " +
                            "Remove or reassign that profile first.");
        }
    }

    @Override
    public List<Demographic> getAll() {
        return repository.findAll();
    }

    private void assertNotDuplicate(Demographic demographic, String ownId) {
        String genderId = genderId(demographic.getGender());
        String raceId = raceId(demographic.getRace());
        for (Demographic existing : repository.findAll()) {
            if (Objects.equals(existing.getDemographyId(), ownId)) {
                continue;
            }
            if (Objects.equals(genderId, genderId(existing.getGender()))
                    && Objects.equals(raceId, raceId(existing.getRace()))) {
                throw new IllegalStateException(
                        "A demographic with this gender and race already exists.");
            }
        }
    }

    private static String genderId(Gender gender) {
        return gender == null ? null : gender.getGenderId();
    }

    private static String raceId(Race race) {
        return race == null ? null : race.getRaceId();
    }
}
