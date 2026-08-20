package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilitySlotFactoryTest {

    @Test
    void createAvailabilitySlot() {

        User trainer = new User.Builder()
                .setFirstName("Phumelela")
                .setLastName("Sakie")
                .setDateOfBirth(LocalDate.of(2005, 1, 15))
                .build();

        AvailabilitySlot slot = AvailabilitySlotFactory.createAvailabilitySlot(
                "SLOT001",
                LocalDate.of(2026, 8, 20),
                LocalDateTime.of(2026, 8, 20, 9, 0),
                LocalDateTime.of(2026, 8, 20, 10, 0),
                SlotStatus.AVAILABLE,
                trainer
        );

        assertNotNull(slot);
        assertEquals("SLOT001", slot.getSlotId());
        assertEquals(LocalDate.of(2026, 8, 20), slot.getDate());
        assertEquals(
                LocalDateTime.of(2026, 8, 20, 9, 0),
                slot.getStartTime()
        );
        assertEquals(
                LocalDateTime.of(2026, 8, 20, 10, 0),
                slot.getEndTime()
        );
        assertEquals(SlotStatus.AVAILABLE, slot.getStatus());
        assertEquals(trainer, slot.getTrainer());
    }
}