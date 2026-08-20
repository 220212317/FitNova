package za.ac.cput.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import za.ac.cput.domain.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Author: Phumelela Sakie (240040546)
 */

@Entity
public class AvailabilitySlot {

    @Id
    private String slotId;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    // Many AvailabilitySlots can belong to one User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    // One AvailabilitySlot can have many Bookings
    @OneToMany(mappedBy = "slot")
    private List<Booking> bookings = new ArrayList<>();

    protected AvailabilitySlot() {
    }

    public AvailabilitySlot(Builder builder) {
        this.slotId = builder.slotId;
        this.date = builder.date;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.status = builder.status;
        this.trainer = builder.trainer;
        this.bookings = builder.bookings;
    }

    public String getSlotId() {
        return slotId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public User getTrainer() {
        return trainer;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public static class Builder {

        private String slotId;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private SlotStatus status;
        private User trainer;
        private List<Booking> bookings = new ArrayList<>();

        public Builder setSlotId(String slotId) {
            this.slotId = slotId;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setStatus(SlotStatus status) {
            this.status = status;
            return this;
        }

        public Builder setTrainer(User trainer) {
            this.trainer = trainer;
            return this;
        }

        public Builder setBookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }

        public Builder copy(AvailabilitySlot availabilitySlot) {
            this.slotId = availabilitySlot.slotId;
            this.date = availabilitySlot.date;
            this.startTime = availabilitySlot.startTime;
            this.endTime = availabilitySlot.endTime;
            this.status = availabilitySlot.status;
            this.trainer = availabilitySlot.trainer;
            this.bookings = availabilitySlot.bookings;
            return this;
        }

        public AvailabilitySlot build() {
            return new AvailabilitySlot(this);
        }
    }
}
