package za.ac.cput.factory;

import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/*
 * Author: Phumelela Sakie (240040546)
 */

public class AvailabilitySlotFactory {

    public static AvailabilitySlot createAvailabilitySlot(
            String slotId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            SlotStatus status,
            User trainer) {

        if (slotId == null || slotId.isEmpty()) {
            throw new IllegalArgumentException("Slot ID is required");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("End time is required");
        }

        if (status == null) {
            throw new IllegalArgumentException("Slot status is required");
        }

        if (trainer == null) {
            throw new IllegalArgumentException("Trainer is required");
        }

        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException(
                    "End time cannot be before start time"
            );
        }

        return new AvailabilitySlot.Builder()
                .setSlotId(slotId)
                .setDate(date)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setStatus(status)
                .setTrainer(trainer)
                .build();
    }
}