package za.ac.cput.factory;

import za.ac.cput.entity.*;
import za.ac.cput.entity.enums.BookingStatus;

import java.time.LocalDateTime;

/*
* Author: Collins Shibambo (230093183)
* */
public class BookingFactory {

    public static Booking createBooking(String bookingId, LocalDateTime bookingDateTime, BookingStatus status, Member member, Trainer trainer, AvailabilitySlot slot) {

        if (bookingId == null || bookingId.isEmpty()) {
            return null;
        }

        if (bookingDateTime == null) {
            return null;
        }

        if (status == null) {
            return null;
        }

        if (member == null || trainer == null || slot == null) {
            return null;
        }
        return new Booking.Builder()
                .bookingId(bookingId)
                .bookingDateTime(bookingDateTime)
                .status(status)
                .member(member)
                .trainer(trainer)
                .slot(slot)
                .build();
    }
}
