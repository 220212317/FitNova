package za.ac.cput.factory;
import za.ac.cput.entity.AvailabilitySlot;
import za.ac.cput.entity.enums.SlotStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
/*
 * Author: Phumelela Sakie (240040546)
 * */

import za.ac.cput.entity.AvailabilitySlot;
import za.ac.cput.entity.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Author: Phumelela Sakie (240040546)
 */

public class AvailabilitySlotFactory {

    public static AvailabilitySlot createSlot(LocalDate date,
                                              LocalDateTime startTime,
                                              LocalDateTime endTime) {

        if (date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("Date and time cannot be null");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        if (!startTime.toLocalDate().equals(date)) {
            throw new IllegalArgumentException("Date must match start time");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create slot in the past");
        }

        String slotId = UUID.randomUUID().toString();

        return new AvailabilitySlot.Builder()
                .setSlotId(slotId)
                .setDate(date)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setStatus(SlotStatus.AVAILABLE)
                .build();
    }
}
