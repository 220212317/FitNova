package za.ac.cput.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.domain.enums.SlotStatus;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.service.IBookingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Avuyile Sitoyi
 * 240971051
 *
 * */
@SpringBootTest
class BookingServiceImplTest {
    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAvailabilitySlotRepository availabilitySlotRepository;

    private User persistedMember() {
        User member = new User.Builder()
                .setUserId("U-" + UUID.randomUUID())
                .setFirstName("Lisakhanya")
                .setLastName("Tshokolo")
                .setDateOfBirth(LocalDate.of(2000, 3, 14))
                .build();
        return userRepository.save(member);
    }

    private User persistedTrainer() {
        User trainer = new User.Builder()
                .setUserId("U-" + UUID.randomUUID())
                .setFirstName("Athi")
                .setLastName("Gatyeni")
                .setDateOfBirth(LocalDate.of(1990, 6, 1))
                .build();
        return userRepository.save(trainer);
    }

    private AvailabilitySlot persistedSlot() {
        AvailabilitySlot slot = new AvailabilitySlot.Builder()
                .setSlotId("S-" + UUID.randomUUID())
                .setDate(LocalDate.now().plusDays(1))
                .setStartTime(LocalTime.of(8, 0))
                .setEndTime(LocalTime.of(9, 0))
                .setStatus(SlotStatus.AVAILABLE)
                .setTrainer(persistedTrainer())
                .build();
        return availabilitySlotRepository.save(slot);
    }

    private Booking buildBooking(User member, AvailabilitySlot slot, BookingStatus status) {
        return new Booking.Builder()
                .setBookingId("B-" + UUID.randomUUID())
                .setBookingDateTime(LocalDateTime.now().plusDays(1))
                .setStatus(status)
                .setMember(member)
                .setSlot(slot)
                .build();
    }

    @Test
    void create() {
        Booking booking = buildBooking(persistedMember(), persistedSlot(), BookingStatus.CONFIRMED);

        Booking created = bookingService.create(booking);

        assertNotNull(created);
        assertEquals(booking.getBookingId(), created.getBookingId());
        assertEquals(BookingStatus.CONFIRMED, created.getStatus());
    }

    @Test
    void read() {
        Booking booking = bookingService.create(buildBooking(persistedMember(), persistedSlot(), BookingStatus.CONFIRMED));

        Booking read = bookingService.read(booking.getBookingId());

        assertNotNull(read);
        assertEquals(booking.getBookingId(), read.getBookingId());
    }

    @Test
    void update() {
        Booking booking = bookingService.create(buildBooking(persistedMember(), persistedSlot(), BookingStatus.CONFIRMED));

        Booking changed = new Booking.Builder()
                .copy(booking)
                .setStatus(BookingStatus.COMPLETED)
                .build();

        Booking updated = bookingService.update(changed);

        assertNotNull(updated);
        assertEquals(BookingStatus.COMPLETED, updated.getStatus());
    }

    @Test
    void delete() {
        Booking booking = bookingService.create(buildBooking(persistedMember(), persistedSlot(), BookingStatus.CONFIRMED));

        boolean deleted = bookingService.delete(booking.getBookingId());

        assertTrue(deleted);
        assertNull(bookingService.read(booking.getBookingId()));
    }

    @Test
    void getAll() {
        Booking booking = bookingService.create(buildBooking(persistedMember(), persistedSlot(), BookingStatus.CONFIRMED));

        List<Booking> bookings = bookingService.getAll();

        assertNotNull(bookings);
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(booking.getBookingId())));
    }

    @Test
    void getBookingsByMember() {
        User member = persistedMember();
        Booking booking = bookingService.create(buildBooking(member, persistedSlot(), BookingStatus.CONFIRMED));

        List<Booking> bookings = bookingService.getBookingsByMember(member.getUserId());

        assertNotNull(bookings);
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(booking.getBookingId())));
    }

    @Test
    void getBookingsBySlot() {
        AvailabilitySlot slot = persistedSlot();
        Booking booking = bookingService.create(buildBooking(persistedMember(), slot, BookingStatus.CONFIRMED));

        List<Booking> bookings = bookingService.getBookingsBySlot(slot.getSlotId());

        assertNotNull(bookings);
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(booking.getBookingId())));
    }

    @Test
    void getBookingsByStatus() {
        Booking booking = bookingService.create(buildBooking(persistedMember(), persistedSlot(), BookingStatus.CANCELLED));

        List<Booking> bookings = bookingService.getBookingsByStatus(BookingStatus.CANCELLED);

        assertNotNull(bookings);
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(booking.getBookingId())));
    }



}