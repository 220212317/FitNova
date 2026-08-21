package za.ac.cput.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.service.IAvailabilitySlotService;

import java.util.List;

@Service
public class AvailabilitySlotServiceImpl implements IAvailabilitySlotService {

    private final IAvailabilitySlotRepository repository;

    public AvailabilitySlotServiceImpl(IAvailabilitySlotRepository repository) {
        this.repository = repository;
    }

    @Override
    public AvailabilitySlot create(AvailabilitySlot availabilitySlot) {
        return repository.save(availabilitySlot);
    }

    @Override
    public AvailabilitySlot read(String slotId) {
        return repository.findById(slotId).orElse(null);
    }

    @Override
    public List<AvailabilitySlot> readAll() {
        return repository.findAll();
    }

    @Override
    public AvailabilitySlot update(AvailabilitySlot availabilitySlot) {
        return repository.save(availabilitySlot);
    }

    @Override
    public boolean delete(String slotId) {
        if (repository.existsById(slotId)) {
            repository.deleteById(slotId);
            return true;
        }
        return false;
    }
}