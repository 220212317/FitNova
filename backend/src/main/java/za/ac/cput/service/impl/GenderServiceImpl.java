package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Gender;
import za.ac.cput.repository.IGenderRepository;
import za.ac.cput.service.IGenderService;
import za.ac.cput.util.Helper;
import za.ac.cput.util.LookupNames;

import java.util.List;
import java.util.Objects;

/*
 * Author: Inga Plati
 * 230126634
 */

@Service
public class GenderServiceImpl implements IGenderService {

    private final IGenderRepository repository;

    @Autowired
    public GenderServiceImpl(IGenderRepository repository) {
        this.repository = repository;
    }

    @Override
    public synchronized Gender create(Gender gender) {
        if (gender == null) {
            return null;
        }
        String description = LookupNames.clean(gender.getDescription());
        if (LookupNames.key(description).isEmpty()) {
            throw new IllegalArgumentException("Gender description is required.");
        }
        assertNotDuplicate(description, gender.getGenderId());
        String id = gender.getGenderId();
        if (Helper.isNullOrEmpty(id)) {
            id = Helper.generateId();
        }
        return repository.save(new Gender.Builder()
                .copy(gender)
                .setGenderId(id)
                .setDescription(description)
                .build());
    }

    @Override
    public Gender read(String genderId) {
        if (genderId == null) {
            return null;
        }
        return repository.findById(genderId).orElse(null);
    }

    @Override
    public synchronized Gender update(Gender gender) {
        if (gender == null || Helper.isNullOrEmpty(gender.getGenderId())) {
            return null;
        }
        if (!repository.existsById(gender.getGenderId())) {
            return null;
        }
        String description = LookupNames.clean(gender.getDescription());
        if (LookupNames.key(description).isEmpty()) {
            throw new IllegalArgumentException("Gender description is required.");
        }
        assertNotDuplicate(description, gender.getGenderId());
        return repository.save(new Gender.Builder()
                .copy(gender)
                .setDescription(description)
                .build());
    }

    @Override
    public boolean delete(String genderId) {
        if (genderId == null || !repository.existsById(genderId)) {
            return false;
        }
        try {
            repository.deleteById(genderId);
            return true;
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "Cannot delete this gender because it is still used by one or more demographic records. " +
                            "Remove or reassign those profiles first.");
        }
    }

    @Override
    public List<Gender> getAll() {
        return repository.findAll();
    }


    private void assertNotDuplicate(String description, String ownId) {
        String key = LookupNames.key(description);
        for (Gender existing : repository.findAll()) {
            if (Objects.equals(existing.getGenderId(), ownId)) {
                continue;
            }
            if (key.equals(LookupNames.key(existing.getDescription()))) {
                throw new IllegalStateException(
                        "A gender called \"" + existing.getDescription() + "\" already exists.");
            }
        }
    }
}