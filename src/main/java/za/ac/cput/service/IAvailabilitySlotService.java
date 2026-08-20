package za.ac.cput.service;

import za.ac.cput.domain.AvailabilitySlot;

import java.util.List;

public interface IAvailabilitySlotService {

    AvailabilitySlot create(AvailabilitySlot availabilitySlot);

    AvailabilitySlot read(String slotId);

    List<AvailabilitySlot> readAll();

    AvailabilitySlot update(AvailabilitySlot availabilitySlot);

    boolean delete(String slotId);
}
