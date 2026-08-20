package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.repository.IBookingRepository;
import za.ac.cput.service.IBookingService;

import java.util.List;

/*
 * Avuyile Sitoyi
 * 240971051
 *
 * */
@Service
public class BookingServiceImpl implements IBookingService {

    private final IBookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(IBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking create(Booking booking) {
        if (booking == null) {
            return null;
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking read(String id) {
        if (id == null) {
            return null;
        }
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking update(Booking booking) {
        if (booking == null || booking.getBookingId() == null) {
            return null;
        }
        if (!bookingRepository.existsById(booking.getBookingId())) {
            return null;
        }
        return bookingRepository.save(booking);
    }

    @Override
    public boolean delete(String id) {
        if (id == null || !bookingRepository.existsById(id)) {
            return false;
        }
        bookingRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByMember(String userId) {
        return bookingRepository.findByMember_UserId(userId);
    }

    @Override
    public List<Booking> getBookingsBySlot(String slotId) {
        return bookingRepository.findBySlot_SlotId(slotId);
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);

    }

}