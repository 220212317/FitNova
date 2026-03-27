package za.ac.cput.entity;

import za.ac.cput.entity.enums.BookingStatus;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
/*
* BookingTest.java - Test class for Booking entity
* Author: Collins Shibambo (230093183)
* */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingTest {

    private static Booking booking;

    @BeforeAll
    static void setUp() {

        booking = new Booking.Builder()
                .bookingId("B001")
                .bookingDateTime(LocalDateTime.now())
                .status(BookingStatus.ACTIVE)
                .member(null)
                .trainer(null)
                .slot(null)
                .build();
    }

    @Test
    @Order(1)
    void build() {
        assertNotNull(booking);
        System.out.println(booking);
    }

    @Test
    @Order(2)
    void getBookingId() {
        assertEquals("B001", booking.getBookingId());
    }

    @Test
    @Order(3)
    void getStatus() {
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
    }
}