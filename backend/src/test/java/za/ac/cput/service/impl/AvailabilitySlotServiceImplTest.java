package za.ac.cput.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.domain.enums.SlotStatus;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.security.adapter.AuthenticatedUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilitySlotServiceImplTest {

    @Mock
    private IAvailabilitySlotRepository repository;

    @Mock
    private IUserRepository userRepository;

    private AutoCloseable mocks;
    private AvailabilitySlotServiceImpl service;

    private AvailabilitySlot slot;
    private User trainer;

    @BeforeEach
    void setUp() {

        mocks = MockitoAnnotations.openMocks(this);

        trainer = new User.Builder()
                .setUserId("User001")
                .setFirstName("Phumelela")
                .setLastName("Sakie")
                .setDateOfBirth(LocalDate.of(2005, 1, 15))
                .build();

        slot = new AvailabilitySlot.Builder()
                .setSlotId("SLOT001")
                .setDate(LocalDate.now().plusDays(7))
                .setStartTime(LocalTime.of(9, 0))
                .setEndTime(LocalTime.of(10, 0))
                .setStatus(SlotStatus.AVAILABLE)
                .setTrainer(trainer)
                .build();

        service = new AvailabilitySlotServiceImpl(
                repository,
                userRepository
        );

        AuthenticatedUser authenticatedTrainer = new AuthenticatedUser.Builder()
                .setUserId(trainer.getUserId())
                .setEmail("trainer@example.com")
                .setDisplayName("Phumelela Sakie")
                .setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_TRAINER")))
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        authenticatedTrainer, "test", authenticatedTrainer.getAuthorities()));
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void create() {

        when(userRepository.findById(trainer.getUserId()))
                .thenReturn(Optional.of(trainer));
        when(repository.findByTrainer_UserId(trainer.getUserId()))
                .thenReturn(List.of());
        when(repository.save(any(AvailabilitySlot.class)))
                .thenReturn(slot);

        AvailabilitySlot created = service.create(slot);

        assertNotNull(created);
        assertEquals("SLOT001", created.getSlotId());
        assertEquals(SlotStatus.AVAILABLE, created.getStatus());
        assertEquals(trainer, created.getTrainer());

        verify(userRepository, times(1))
                .findById(trainer.getUserId());

        verify(repository, times(1))
                .save(any(AvailabilitySlot.class));
    }

    @Test
    void read() {

        when(repository.findById("SLOT001"))
                .thenReturn(Optional.of(slot));

        AvailabilitySlot found = service.read("SLOT001");

        assertNotNull(found);
        assertEquals("SLOT001", found.getSlotId());

        verify(repository, times(1))
                .findById("SLOT001");
    }

    @Test
    void readAll() {

        when(repository.findAll())
                .thenReturn(Arrays.asList(slot));

        var slots = service.readAll();

        assertNotNull(slots);
        assertEquals(1, slots.size());
        assertEquals("SLOT001", slots.get(0).getSlotId());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void update() {

        when(repository.findById("SLOT001"))
                .thenReturn(Optional.of(slot));
        when(repository.findByTrainer_UserId(trainer.getUserId()))
                .thenReturn(List.of());
        when(repository.save(any(AvailabilitySlot.class)))
                .thenReturn(slot);

        AvailabilitySlot updated = service.update(slot);

        assertNotNull(updated);
        assertEquals("SLOT001", updated.getSlotId());
        assertEquals(trainer, updated.getTrainer());

        verify(repository, times(1))
                .findById("SLOT001");
        verify(repository, times(1))
                .save(any(AvailabilitySlot.class));
    }

    @Test
    void delete() {

        when(repository.findById("SLOT001"))
                .thenReturn(Optional.of(slot));

        boolean deleted = service.delete("SLOT001");

        assertTrue(deleted);

        verify(repository, times(1))
                .delete(slot);
    }

    private AvailabilitySlot slotWithBooking(BookingStatus bookingStatus) {
        Booking booking = new Booking.Builder()
                .setBookingId("BOOK001")
                .setStatus(bookingStatus)
                .build();

        return new AvailabilitySlot.Builder()
                .copy(slot)
                .setBookings(new java.util.ArrayList<>(List.of(booking)))
                .build();
    }

    @Test
    void delete_allowedWhenOnlyCancelledBookingsRemain() {

        AvailabilitySlot withCancelled = slotWithBooking(BookingStatus.CANCELLED);
        when(repository.findById("SLOT001")).thenReturn(Optional.of(withCancelled));

        assertTrue(service.delete("SLOT001"));
        verify(repository, times(1)).delete(withCancelled);
    }

    @Test
    void delete_blockedForConfirmedInProgressOrCompletedBookings() {

        for (BookingStatus status : List.of(
                BookingStatus.CONFIRMED,
                BookingStatus.IN_PROGRESS,
                BookingStatus.COMPLETED)) {

            AvailabilitySlot protectedSlot = slotWithBooking(status);
            when(repository.findById("SLOT001")).thenReturn(Optional.of(protectedSlot));

            assertFalse(service.delete("SLOT001"), "Should be protected: " + status);
            verify(repository, never()).delete(protectedSlot);
        }
    }

    @Test
    void delete_anotherTrainersSlotIsForbidden() {

        User otherTrainer = new User.Builder()
                .setUserId("User002")
                .setFirstName("Other")
                .setLastName("Trainer")
                .setDateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        AvailabilitySlot othersSlot = new AvailabilitySlot.Builder()
                .copy(slot)
                .setTrainer(otherTrainer)
                .build();

        when(repository.findById("SLOT001")).thenReturn(Optional.of(othersSlot));

        assertThrows(IllegalStateException.class, () -> service.delete("SLOT001"));
        verify(repository, never()).delete(any(AvailabilitySlot.class));
    }
}
