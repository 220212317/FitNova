package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Gender;
import za.ac.cput.repository.IGenderRepository;
import za.ac.cput.service.IGenderService;
import za.ac.cput.util.Helper;

import java.util.List;

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
    public Gender create(Gender gender) {
        if (gender == null) {
            return null;
        }
        if (Helper.isNullOrEmpty(gender.getDescription())) {
            throw new IllegalArgumentException("Gender description is required.");
        }
        String id = gender.getGenderId();
        if (Helper.isNullOrEmpty(id)) {
            gender = new Gender.Builder()
                    .copy(gender)
                    .setGenderId(Helper.generateId())
                    .build();
        }
        return repository.save(gender);
    }

    @Override
    public Gender read(String genderId) {
        if (genderId == null) {
            return null;
        }
        return repository.findById(genderId).orElse(null);
    }

    @Override
    public Gender update(Gender gender) {
        if (gender == null || Helper.isNullOrEmpty(gender.getGenderId())) {
            return null;
        }
        if (!repository.existsById(gender.getGenderId())) {
            return null;
        }
        if (Helper.isNullOrEmpty(gender.getDescription())) {
            throw new IllegalArgumentException("Gender description is required.");
        }
        return repository.save(gender);
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
}