package za.ac.cput.service;

import za.ac.cput.domain.Booking;
import za.ac.cput.domain.enums.BookingStatus;

import java.util.List;

public interface IBookingService extends IService<Booking, String> {

    List<Booking> getBookingsByMember(String userId);

    List<Booking> getBookingsBySlot(String slotId);

    List<Booking> getBookingsByStatus(BookingStatus status);

    List<Booking> getBookingsByTrainer(String trainerId);

    Booking startSession(String bookingId, String trainerId);

    Booking completeSession(String bookingId, String trainerId);
}