package za.ac.cput.service;

import za.ac.cput.domain.Booking;
import za.ac.cput.domain.enums.BookingStatus;

import java.util.List;
/*
 * Avuyile Sitoyi
 * 240971051
 *
 * */
public interface IBookingService extends IService<Booking,String>{
    List<Booking> getBookingsByMember(String userId);
    List<Booking> getBookingsBySlot(String slotId);
    List<Booking> getBookingsByStatus(BookingStatus status);
}
