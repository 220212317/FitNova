package za.ac.cput.factory;
import za.ac.cput.entity.AvailabilitySlot;
import za.ac.cput.entity.enums.SlotStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
/*
 * Author: Phumelela Sakie (240040546)
 * */

public class AvailabilitySlotFactory {

    public static AvailabilitySlot createSlot(LocalDate date,
                                              LocalDateTime startTime,
                                              LocalDateTime endTime) {

        if (date == null || startTime == null || endTime == null) {
            return null;
        }


        if (!endTime.isAfter(startTime)) {
            return null;
        }


        if (!startTime.toLocalDate().equals(date)) {
            return null;
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
