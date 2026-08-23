package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Gender;
import za.ac.cput.repository.IGenderRepository;
import za.ac.cput.service.IGenderService;

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
        if (gender == null || gender.getGenderId() == null) {
            return null;
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
        if (gender == null || gender.getGenderId() == null) {
            return null;
        }
        if (!repository.existsById(gender.getGenderId())) {
            return null;
        }
        return repository.save(gender);
    }

    @Override
    public boolean delete(String genderId) {
        if (genderId == null || !repository.existsById(genderId)) {
            return false;
        }
        repository.deleteById(genderId);
        return true;
    }

    @Override
    public List<Gender> getAll() {
        return repository.findAll();
    }
}