package za.ac.cput.factory;

import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;
/*
* Avuyile Sitoyi
* 240971051
*
* */
public class BookingFactory {
    public static Booking createBooking(String bookingId,
                                        LocalDateTime bookingDateTime,
                                        BookingStatus status,
                                        User member,
                                        AvailabilitySlot slot) {

        if (bookingDateTime == null) {
            return null;
        }
        if (status == null) {
            return null;
        }
        if (member == null) {
            return null;
        }
        if (slot == null) {
            return null;
        }

        String finalId;
        if (bookingId == null || bookingId.trim().isEmpty()) {
            finalId = Helper.generateId();
        } else {
            finalId = bookingId;
        }

        return new Booking.Builder()
                .setBookingId(finalId)
                .setBookingDateTime(bookingDateTime)
                .setStatus(status)
                .setMember(member)
                .setSlot(slot)
                .build();


    }

}
