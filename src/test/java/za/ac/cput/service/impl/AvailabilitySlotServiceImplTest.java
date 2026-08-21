package za.ac.cput.service.impl;

import static org.junit.jupiter.api.Assertions.*;




import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.SlotStatus;
import za.ac.cput.repository.IAvailabilitySlotRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilitySlotServiceImplTest {

    @Mock
    private IAvailabilitySlotRepository repository;

    private AvailabilitySlotServiceImpl service;

    private AvailabilitySlot slot;
    private User trainer;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        trainer = new User.Builder()
                .setFirstName("Phumelela")
                .setLastName("Sakie")
                .setDateOfBirth(LocalDate.of(2005, 1, 15))
                .build();

        slot = new AvailabilitySlot.Builder()
                .setSlotId("SLOT001")
                .setDate(LocalDate.of(2026, 8, 21))
                .setStartTime(LocalTime.of(9, 0))
                .setEndTime(LocalTime.of(10, 0))
                .setStatus(SlotStatus.AVAILABLE)
                .setTrainer(trainer)
                .build();

        service = new AvailabilitySlotServiceImpl(repository);
    }

    @Test
    void create() {

        when(repository.save(slot)).thenReturn(slot);

        AvailabilitySlot created = service.create(slot);

        assertNotNull(created);
        assertEquals("SLOT001", created.getSlotId());
        assertEquals(SlotStatus.AVAILABLE, created.getStatus());

        verify(repository, times(1)).save(slot);
    }

    @Test
    void read() {

        when(repository.findById("SLOT001"))
                .thenReturn(Optional.of(slot));

        AvailabilitySlot found = service.read("SLOT001");

        assertNotNull(found);
        assertEquals("SLOT001", found.getSlotId());

        verify(repository, times(1)).findById("SLOT001");
    }

    @Test
    void readAll() {

        when(repository.findAll())
                .thenReturn(Arrays.asList(slot));

        var slots = service.readAll();

        assertNotNull(slots);
        assertEquals(1, slots.size());
        assertEquals("SLOT001", slots.get(0).getSlotId());

        verify(repository, times(1)).findAll();
    }

    @Test
    void update() {

        when(repository.save(slot)).thenReturn(slot);

        AvailabilitySlot updated = service.update(slot);

        assertNotNull(updated);
        assertEquals("SLOT001", updated.getSlotId());

        verify(repository, times(1)).save(slot);
    }

    @Test
    void delete() {

        when(repository.existsById("SLOT001"))
                .thenReturn(true);

        boolean deleted = service.delete("SLOT001");

        assertTrue(deleted);

        verify(repository, times(1))
                .deleteById("SLOT001");
    }
}