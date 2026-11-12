package za.ac.cput.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.domain.enums.SlotStatus;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IBookingRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.service.IBookingService;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements IBookingService {

    private final IBookingRepository bookingRepository;
    private final IUserRepository userRepository;
    private final IAvailabilitySlotRepository slotRepository;

    public BookingServiceImpl(
            IBookingRepository bookingRepository,
            IUserRepository userRepository,
            IAvailabilitySlotRepository slotRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public Booking create(Booking booking) {
        if (booking == null || booking.getStatus() == null) {
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
        if (slot == null || slot.getStatus() != SlotStatus.AVAILABLE) {
            return null;
        }

        String bookingId = booking.getBookingId();
        if (Helper.isNullOrEmpty(bookingId)) {
            bookingId = Helper.generateId();
        }

        LocalDateTime bookingDateTime = booking.getBookingDateTime();
        if (bookingDateTime == null) {
            bookingDateTime = LocalDateTime.now();
        }

        // Reserve the slot so it cannot be double-booked.
        AvailabilitySlot bookedSlot = new AvailabilitySlot.Builder()
                .copy(slot)
                .setStatus(SlotStatus.BOOKED)
                .build();
        bookedSlot = slotRepository.save(bookedSlot);

        Booking toSave = new Booking.Builder()
                .setBookingId(bookingId)
                .setBookingDateTime(bookingDateTime)
                .setStatus(BookingStatus.CONFIRMED)
                .setMember(member)
                .setSlot(bookedSlot)
                .build();

        return bookingRepository.save(toSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking read(String id) {
        if (id == null) {
            return null;
        }
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking update(Booking booking) {
        if (booking == null
                || Helper.isNullOrEmpty(booking.getBookingId())
                || booking.getStatus() == null) {
            return null;
        }

        Booking existing = bookingRepository.findById(booking.getBookingId()).orElse(null);
        if (existing == null) {
            return null;
        }


        BookingStatus previousStatus = existing.getStatus();
        existing.setStatus(booking.getStatus());

        if (booking.getBookingDateTime() != null) {
            existing.setBookingDateTime(booking.getBookingDateTime());
        }

        // When a booking is cancelled, free the slot for others.
        if (booking.getStatus() == BookingStatus.CANCELLED
                && previousStatus != BookingStatus.CANCELLED
                && existing.getSlot() != null) {
            AvailabilitySlot slot = existing.getSlot();
            if (slot.getStatus() == SlotStatus.BOOKED) {
                AvailabilitySlot freed = new AvailabilitySlot.Builder()
                        .copy(slot)
                        .setStatus(SlotStatus.AVAILABLE)
                        .build();
                slotRepository.save(freed);
            }
        }

        return bookingRepository.save(existing);
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
    @Transactional(readOnly = true)
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByMember(String userId) {
        return bookingRepository.findByMember_UserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsBySlot(String slotId) {
        return bookingRepository.findBySlot_SlotId(slotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByTrainer(String trainerId) {
        if (Helper.isNullOrEmpty(trainerId)) {
            return List.of();
        }

        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getSlot() != null)
                .filter(booking -> booking.getSlot().getTrainer() != null)
                .filter(booking -> trainerId.equals(
                        booking.getSlot().getTrainer().getUserId()
                ))
                .map(this::withResolvedMember)
                .toList();
    }


    private Booking withResolvedMember(Booking booking) {
        if (booking == null || booking.getMember() == null) {
            return booking;
        }
        String memberId = booking.getMember().getUserId();
        if (Helper.isNullOrEmpty(memberId)) {
            return booking;
        }
        User fullMember = userRepository.findById(memberId).orElse(null);
        if (fullMember == null) {
            return booking;
        }
        booking.setMember(fullMember);
        return booking;
    }

    @Override
    public Booking startSession(String bookingId, String trainerId) {
        Booking booking = findTrainerBooking(bookingId, trainerId);
        if (booking == null) {
            return null;
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed bookings can be started."
            );
        }

        AvailabilitySlot slot = booking.getSlot();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime slotStart = LocalDateTime.of(slot.getDate(), slot.getStartTime());
        LocalDateTime slotEnd = LocalDateTime.of(slot.getDate(), slot.getEndTime());

        if (now.isBefore(slotStart)) {
            throw new IllegalStateException(
                    "You cannot start the session before its scheduled start time."
            );
        }

        if (!now.isBefore(slotEnd)) {
            throw new IllegalStateException(
                    "The scheduled slot has ended; this session can no longer be started."
            );
        }

        booking.setStatus(BookingStatus.IN_PROGRESS);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking completeSession(String bookingId, String trainerId) {
        Booking booking = findTrainerBooking(bookingId, trainerId);
        if (booking == null) {
            return null;
        }

        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Only in-progress sessions can be completed."
            );
        }

        AvailabilitySlot slot = booking.getSlot();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime slotEnd = LocalDateTime.of(slot.getDate(), slot.getEndTime());

        if (now.isBefore(slotEnd)) {
            throw new IllegalStateException(
                    "You can complete the session when its scheduled end time has arrived."
            );
        }

        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }

    private Booking findTrainerBooking(String bookingId, String trainerId) {
        if (Helper.isNullOrEmpty(bookingId) || Helper.isNullOrEmpty(trainerId)) {
            return null;
        }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            return null;
        }

        AvailabilitySlot slot = booking.getSlot();
        User trainer = slot == null ? null : slot.getTrainer();

        if (trainer == null || !trainerId.equals(trainer.getUserId())) {
            throw new IllegalStateException(
                    "You can manage sessions only for bookings on your own slots."
            );
        }

        if (slot.getDate() == null
                || slot.getStartTime() == null
                || slot.getEndTime() == null) {
            throw new IllegalStateException(
                    "The booking slot is missing its scheduled date or time."
            );
        }

        return booking;
    }
}