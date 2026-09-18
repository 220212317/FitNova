package za.ac.cput.service.impl;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.User;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.service.IAvailabilitySlotService;
import za.ac.cput.util.Helper;

import java.util.List;

@Service
public class AvailabilitySlotServiceImpl implements IAvailabilitySlotService {

    private final IAvailabilitySlotRepository repository;
    private final IUserRepository userRepository;

    public AvailabilitySlotServiceImpl(
            IAvailabilitySlotRepository repository,
            IUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public AvailabilitySlot create(AvailabilitySlot availabilitySlot) {
        if (!isValidSlot(availabilitySlot)
                || availabilitySlot.getTrainer() == null
                || availabilitySlot.getTrainer().getUserId() == null) {
            return null;
        }

        String trainerId = availabilitySlot.getTrainer().getUserId();
        User trainer = userRepository.findById(trainerId).orElse(null);

        if (trainer == null) {
            return null;
        }

        String slotId = availabilitySlot.getSlotId();

        if (slotId == null || slotId.isBlank()) {
            slotId = Helper.generateId();
        }

        AvailabilitySlot normalizedSlot = new AvailabilitySlot.Builder()
                .copy(availabilitySlot)
                .setTrainer(trainer)
                .setSlotId(slotId)
                .build();

        return repository.save(normalizedSlot);
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
        if (!isValidSlot(availabilitySlot)) {
            return null;
        }

        if (availabilitySlot.getSlotId() == null
                || !repository.existsById(availabilitySlot.getSlotId())) {
            return null;
        }

        AvailabilitySlot normalizedSlot = availabilitySlot;

        if (availabilitySlot.getTrainer() != null
                && availabilitySlot.getTrainer().getUserId() != null) {

            String trainerId = availabilitySlot.getTrainer().getUserId();
            User trainer = userRepository.findById(trainerId).orElse(null);

            if (trainer == null) {
                return null;
            }

            normalizedSlot = new AvailabilitySlot.Builder()
                    .copy(availabilitySlot)
                    .setTrainer(trainer)
                    .build();
        }

        return repository.save(normalizedSlot);
    }


    @Override
    public boolean delete(String slotId) {
        if (repository.existsById(slotId)) {
            repository.deleteById(slotId);
            return true;
        }
        return false;
    }

    private boolean isValidSlot(AvailabilitySlot availabilitySlot) {

        if (availabilitySlot == null
                || availabilitySlot.getDate() == null
                || availabilitySlot.getStartTime() == null
                || availabilitySlot.getEndTime() == null
                || availabilitySlot.getStatus() == null) {
            return false;
        }

        return availabilitySlot.getEndTime()
                .isAfter(availabilitySlot.getStartTime());
    }

}