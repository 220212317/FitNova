package za.ac.cput.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.domain.enums.SlotStatus;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.security.adapter.AuthenticatedUser;
import za.ac.cput.service.IAvailabilitySlotService;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AvailabilitySlotServiceImpl implements IAvailabilitySlotService {

    private final IAvailabilitySlotRepository repository;
    private final IUserRepository userRepository;

    public AvailabilitySlotServiceImpl(
            IAvailabilitySlotRepository repository,
            IUserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    private AuthenticatedUser getAuthenticatedTrainer() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof AuthenticatedUser)) {
            throw new IllegalStateException("User is not authenticated.");
        }

        AuthenticatedUser currentUser =
                (AuthenticatedUser) authentication.getPrincipal();

        if (!"TRAINER".equals(currentUser.primaryRole())) {
            throw new IllegalStateException("Only trainers can manage availability slots.");
        }

        return currentUser;
    }

    @Override
    public AvailabilitySlot create(AvailabilitySlot availabilitySlot) {

        AuthenticatedUser currentUser = getAuthenticatedTrainer();

        if (!isValidSlot(availabilitySlot)) {
            return null;
        }

        if (availabilitySlot.getStatus() != SlotStatus.AVAILABLE
                && availabilitySlot.getStatus() != SlotStatus.UNAVAILABLE) {
            return null;
        }

        String trainerId = currentUser.getUserId();

        User trainer = userRepository.findById(trainerId).orElse(null);

        if (trainer == null) {
            return null;
        }

        if (hasOverlappingSlot(availabilitySlot, trainerId, null)) {
            return null;
        }

        String slotId = availabilitySlot.getSlotId();

        if (slotId == null || slotId.isBlank()) {
            slotId = Helper.generateId();
        }

        AvailabilitySlot newSlot = new AvailabilitySlot.Builder()
                .copy(availabilitySlot)
                .setSlotId(slotId)
                .setTrainer(trainer)
                .build();

        return repository.save(newSlot);
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilitySlot read(String slotId) {

        if (slotId == null || slotId.isBlank()) {
            return null;
        }

        return repository.findById(slotId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilitySlot> readAll() {

        // Members need to view trainers' available slots for booking.
        return repository.findAll();
    }

    @Override
    public AvailabilitySlot update(AvailabilitySlot availabilitySlot) {

        AuthenticatedUser currentUser = getAuthenticatedTrainer();

        if (availabilitySlot == null
                || availabilitySlot.getSlotId() == null
                || !isValidSlot(availabilitySlot)) {
            return null;
        }

        AvailabilitySlot existingSlot = repository
                .findById(availabilitySlot.getSlotId())
                .orElse(null);

        if (existingSlot == null) {
            return null;
        }

        String trainerId = currentUser.getUserId();

        // A trainer may only update their own slot.
        if (existingSlot.getTrainer() == null
                || !trainerId.equals(existingSlot.getTrainer().getUserId())) {
            throw new IllegalStateException(
                    "You cannot update another trainer's availability slot.");
        }

        // Do not allow the client to change the trainer or slot ID.
        if (hasProtectedBooking(existingSlot)) {
            return null;
        }

        if (availabilitySlot.getStatus() != SlotStatus.AVAILABLE
                && availabilitySlot.getStatus() != SlotStatus.UNAVAILABLE) {
            return null;
        }

        if (hasOverlappingSlot(
                availabilitySlot, trainerId, existingSlot.getSlotId())) {
            return null;
        }

        AvailabilitySlot updatedSlot = new AvailabilitySlot.Builder()
                .copy(availabilitySlot)
                .setSlotId(existingSlot.getSlotId())
                .setTrainer(existingSlot.getTrainer())
                .build();

        return repository.save(updatedSlot);
    }

    @Override
    public boolean delete(String slotId) {

        AuthenticatedUser currentUser = getAuthenticatedTrainer();

        if (slotId == null || slotId.isBlank()) {
            return false;
        }

        AvailabilitySlot existingSlot = repository
                .findById(slotId)
                .orElse(null);

        if (existingSlot == null) {
            return false;
        }

        String trainerId = currentUser.getUserId();

        if (existingSlot.getTrainer() == null
                || !trainerId.equals(existingSlot.getTrainer().getUserId())) {
            throw new IllegalStateException(
                    "You cannot delete another trainer's availability slot.");
        }

        if (hasProtectedBooking(existingSlot)) {
            return false;
        }

        repository.delete(existingSlot);
        return true;
    }

    private boolean isValidSlot(AvailabilitySlot slot) {

        if (slot == null
                || slot.getDate() == null
                || slot.getStartTime() == null
                || slot.getEndTime() == null
                || slot.getStatus() == null) {
            return false;
        }

        if (!slot.getEndTime().isAfter(slot.getStartTime())) {
            return false;
        }

        LocalDateTime startDateTime =
                LocalDateTime.of(slot.getDate(), slot.getStartTime());

        return startDateTime.isAfter(LocalDateTime.now());
    }

    private boolean hasOverlappingSlot(
            AvailabilitySlot newSlot,
            String trainerId,
            String excludedSlotId) {

        List<AvailabilitySlot> trainerSlots =
                repository.findByTrainer_UserId(trainerId);

        for (AvailabilitySlot existing : trainerSlots) {

            if (excludedSlotId != null
                    && excludedSlotId.equals(existing.getSlotId())) {
                continue;
            }

            if (existing.getStatus() != SlotStatus.AVAILABLE) {
                continue;
            }

            if (!existing.getDate().equals(newSlot.getDate())) {
                continue;
            }

            boolean overlaps =
                    newSlot.getStartTime().isBefore(existing.getEndTime())
                            && newSlot.getEndTime().isAfter(existing.getStartTime());

            if (overlaps) {
                return true;
            }
        }

        return false;
    }

    private boolean hasProtectedBooking(AvailabilitySlot slot) {

        if (slot.getBookings() == null) {
            return false;
        }

        for (Booking booking : slot.getBookings()) {

            if (booking.getStatus() == BookingStatus.CONFIRMED
                    || booking.getStatus() == BookingStatus.COMPLETED) {
                return true;
            }
        }

        return false;
    }
}