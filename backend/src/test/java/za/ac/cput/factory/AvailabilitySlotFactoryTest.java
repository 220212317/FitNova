package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilitySlotFactoryTest {

    @Test
    void createAvailabilitySlot() {
        User trainer = new User.Builder()
                .setUserId("TR001")
                .setFirstName("Phumelela")
                .setLastName("Sakie")
                .setDateOfBirth(LocalDate.of(2005, 1, 15))
                .build();

        LocalDate slotDate = LocalDate.of(2026, 8, 20);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);

        AvailabilitySlot slot = AvailabilitySlotFactory.createAvailabilitySlot(
                "SLOT001",
                slotDate,
                startTime,
                endTime,
                SlotStatus.AVAILABLE,
                trainer
        );

        assertNotNull(slot);
        assertEquals("SLOT001", slot.getSlotId());
        assertEquals(slotDate, slot.getDate());
        assertEquals(startTime, slot.getStartTime());
        assertEquals(endTime, slot.getEndTime());
        assertEquals(SlotStatus.AVAILABLE, slot.getStatus());
        assertEquals(trainer, slot.getTrainer());
    }
}
