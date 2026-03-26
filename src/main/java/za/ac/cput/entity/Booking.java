package za.ac.cput.entity;

import za.ac.cput.entity.enums.BookingStatus;
import java.time.LocalDateTime;

/*
* Author: Collins Shibambo (230093183)
* */
public class Booking {

    private String bookingId;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;
    private Member member;
    private Trainer trainer;
    private AvailabilitySlot slot;

    public Booking(){

    }

  public Booking(Builder builder){
        this.bookingId = builder.bookingId;
        this.bookingDateTime = builder.bookingDateTime;
        this.status = builder.status;
        this.member = builder.member;
        this.trainer = builder.trainer;
        this.slot = builder.slot;
    }

    public String getBookingId() {
        return bookingId;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Member getMember() {
        return member;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public AvailabilitySlot getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", bookingDateTime=" + bookingDateTime +
                ", status=" + status +
                ", member=" + member +
                ", trainer=" + trainer +
                ", slot=" + slot +
                '}';
    }

    public static class Builder {
        private String bookingId;
        private LocalDateTime bookingDateTime;
        private BookingStatus status;
        private Member member;
        private Trainer trainer;
        private AvailabilitySlot slot;

        public Builder bookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder bookingDateTime(LocalDateTime bookingDateTime) {
            this.bookingDateTime = bookingDateTime;
            return this;
        }

        public Builder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder trainer(Trainer trainer) {
            this.trainer = trainer;
            return this;
        }

        public Builder slot(AvailabilitySlot slot) {
            this.slot = slot;
            return this;
        }

        public Builder copy(Booking booking) {
            this.bookingId = booking.bookingId;
            this.bookingDateTime = booking.bookingDateTime;
            this.status = booking.status;
            this.member = booking.member;
            this.trainer = booking.trainer;
            this.slot = booking.slot;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
