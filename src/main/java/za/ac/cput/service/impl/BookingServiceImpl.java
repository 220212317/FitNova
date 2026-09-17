package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IBookingRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.service.IBookingService;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService {

    private final IBookingRepository bookingRepository;
    private final IUserRepository userRepository;
    private final IAvailabilitySlotRepository slotRepository;

    @Autowired
    public BookingServiceImpl(IBookingRepository bookingRepository,
                              IUserRepository userRepository,
                              IAvailabilitySlotRepository slotRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public Booking create(Booking booking) {
        if (booking == null) {
            return null;
        }
        if (booking.getStatus() == null) {
            return null;
        }

        User memberRef = booking.getMember();
        if (memberRef == null || Helper.isNullOrEmpty(memberRef.getUserId())) {
            return null;
        }
        User member = userRepository.findById(memberRef.getUserId()).orElse(null);
        if (member == null) {
            return null;
        }

        AvailabilitySlot slotRef = booking.getSlot();
        if (slotRef == null || Helper.isNullOrEmpty(slotRef.getSlotId())) {
            return null;
        }
        AvailabilitySlot slot = slotRepository.findById(slotRef.getSlotId()).orElse(null);
        if (slot == null) {
            return null;
        }

        String bookingId = booking.getBookingId();
        if (Helper.isNullOrEmpty(bookingId)) {
            bookingId = Helper.generateId();
        }

        LocalDateTime when = booking.getBookingDateTime();
        if (when == null) {
            when = LocalDateTime.now();
        }

        Booking toSave = new Booking.Builder()
                .setBookingId(bookingId)
                .setBookingDateTime(when)
                .setStatus(booking.getStatus())
                .setMember(member)
                .setSlot(slot)
                .build();

        return bookingRepository.save(toSave);
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
        if (booking == null || Helper.isNullOrEmpty(booking.getBookingId())) {
            return null;
        }
        if (!bookingRepository.existsById(booking.getBookingId())) {
            return null;
        }

        User member = booking.getMember();
        if (member != null && !Helper.isNullOrEmpty(member.getUserId())) {
            member = userRepository.findById(member.getUserId()).orElse(member);
        }
        AvailabilitySlot slot = booking.getSlot();
        if (slot != null && !Helper.isNullOrEmpty(slot.getSlotId())) {
            slot = slotRepository.findById(slot.getSlotId()).orElse(slot);
        }

        LocalDateTime when = booking.getBookingDateTime();
        if (when == null) {
            when = LocalDateTime.now();
        }

        Booking toSave = new Booking.Builder()
                .setBookingId(booking.getBookingId())
                .setBookingDateTime(when)
                .setStatus(booking.getStatus())
                .setMember(member)
                .setSlot(slot)
                .build();

        return bookingRepository.save(toSave);
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