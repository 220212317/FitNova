package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Demographic;
import za.ac.cput.repository.IDemographicRepository;
import za.ac.cput.service.IDemographicService;

import java.util.List;

/*
 * Author: Inga Plati
 * 230126634
 */

@Service
public class DemographicServiceImpl implements IDemographicService {

    private final IDemographicRepository repository;

    @Autowired
    public DemographicServiceImpl(IDemographicRepository repository) {
        this.repository = repository;
    }

    @Override
    public Demographic create(Demographic demographic) {
        if (demographic == null || demographic.getDemographyId() == null) {
            return null;
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
    public Demographic update(Demographic demographic) {
        if (demographic == null || demographic.getDemographyId() == null) {
            return null;
        }
        if (!repository.existsById(demographic.getDemographyId())) {
            return null;
        }
        return repository.save(demographic);
    }

    @Override
    public boolean delete(String demographyId) {
        if (demographyId == null || !repository.existsById(demographyId)) {
            return false;
        }
        repository.deleteById(demographyId);
        return true;
    }

    @Override
    public List<Demographic> getAll() {
        return repository.findAll();
    }
}