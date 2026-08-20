/*
 * DemographicServiceImpl.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Demographic;
import za.ac.cput.repository.IDemographicRepository;
import za.ac.cput.service.IDemographicService;

import java.util.List;

@Service
public class DemographicServiceImpl implements IDemographicService {

    private final IDemographicRepository repository;

    @Autowired
    public DemographicServiceImpl(IDemographicRepository repository) {
        this.repository = repository;
    }

    @Override
    public Demographic create(Demographic demographic) {
        return repository.save(demographic);
    }

    @Override
    public Demographic read(String demographyId) {
        return repository.findById(demographyId).orElse(null);
    }

    @Override
    public Demographic update(Demographic demographic) {
        if (!repository.existsById(demographic.getDemographyId())) {
            return null;
        }
        return repository.save(demographic);
    }

    @Override
    public void delete(String demographyId) {
        repository.deleteById(demographyId);
    }

    @Override
    public List<Demographic> getAll() {
        return repository.findAll();
    }
}