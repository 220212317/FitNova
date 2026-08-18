package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class BookingFactoryTest {
    private User buildMember() {
        return new User.Builder()
                .setUserId("U001")
                .setFirstName("Thando")
                .setLastName("Nkosi")
                .setDateOfBirth(LocalDate.of(1999, 5, 20))
                .build();
    }

//    private AvailabilitySlot buildSlot() {
//        return new AvailabilitySlot.Builder()
//                .setSlotId("S001")
//                .setDate(LocalDate.now().plusDays(1))
//                .setStartTime(LocalTime.of(9, 0))
//                .setEndTime(LocalTime.of(10, 0))
//                .setStatus(SlotStatus.AVAILABLE)
//                .build();
//    }

//    @Test
//    void testCreateBooking_ValidData_ReturnsBooking() {
//        User member = buildMember();
//        AvailabilitySlot slot = buildSlot();
//        LocalDateTime bookingDateTime = LocalDateTime.now().plusDays(1);
//
//        Booking booking = BookingFactory.createBooking("B001", bookingDateTime, BookingStatus.CONFIRMED, member, slot);
//
//        assertNotNull(booking);
//        assertEquals("B001", booking.getBookingId());
//        assertEquals(bookingDateTime, booking.getBookingDateTime());
//        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
//        assertEquals(member, booking.getMember());
//        assertEquals(slot, booking.getSlot());
//    }

//    @Test
//    void testCreateBooking_NoIdSupplied_GeneratesId() {
//        Booking booking = BookingFactory.createBooking(null, LocalDateTime.now().plusDays(1),
//                BookingStatus.CONFIRMED, buildMember(), buildSlot());
//
//        assertNotNull(booking);
//        assertFalse(booking.getBookingId() == null || booking.getBookingId().trim().isEmpty());
//    }

//    @Test
//    void testCreateBooking_NullBookingDateTime_ReturnsNull() {
//        Booking booking = BookingFactory.createBooking("B002", null,
//                BookingStatus.CONFIRMED, buildMember(), buildSlot());
//
//        assertNull(booking);
//    }

//    @Test
//    void testCreateBooking_NullStatus_ReturnsNull() {
//        Booking booking = BookingFactory.createBooking("B003", LocalDateTime.now().plusDays(1),
//                null, buildMember(), buildSlot());
//
//        assertNull(booking);
//    }

//    @Test
//    void testCreateBooking_NullMember_ReturnsNull() {
//        Booking booking = BookingFactory.createBooking("B004", LocalDateTime.now().plusDays(1),
//                BookingStatus.CONFIRMED, null, buildSlot());
//
//        assertNull(booking);
//    }

    @Test
    void testCreateBooking_NullSlot_ReturnsNull() {
        Booking booking = BookingFactory.createBooking("B005", LocalDateTime.now().plusDays(1),
                BookingStatus.CONFIRMED, buildMember(), null);

        assertNull(booking);
    }
}
