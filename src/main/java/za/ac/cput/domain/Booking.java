package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import za.ac.cput.domain.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Avuyile Sitoyi
 * 240971051
 */
@Entity
@Table(name = "booking")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking {

    @Id
    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "booking_date_time", nullable = false)
    private LocalDateTime bookingDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"bookings", "availabilitySlots", "userRoles", "hibernateLazyInitializer", "handler"})
    private User member;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    @JsonIgnoreProperties({"bookings", "trainer", "hibernateLazyInitializer", "handler"})
    private AvailabilitySlot slot;

    protected Booking() {

    }

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.bookingDateTime = builder.bookingDateTime;
        this.status = builder.status;
        this.member = builder.member;
        this.slot = builder.slot;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public AvailabilitySlot getSlot() {
        return slot;
    }

    public void setSlot(AvailabilitySlot slot) {
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
/*
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", bookingDateTime=" + bookingDateTime +
                ", status=" + status +
                ", member=" + (member != null ? member.getUserId() : null) +
                ", slot=" + (slot != null ? slot.getSlotId() : null) +
                '}';
    }
*/

    public static class Builder {
        private String bookingId = UUID.randomUUID().toString();
        private LocalDateTime bookingDateTime;
        private BookingStatus status;
        private User member;
        private AvailabilitySlot slot;

        public Builder setBookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder setBookingDateTime(LocalDateTime bookingDateTime) {
            this.bookingDateTime = bookingDateTime;
            return this;
        }

        public Builder setStatus(BookingStatus status) {
            this.status = status;
            return this;
        }

        public Builder setMember(User member) {
            this.member = member;
            return this;
        }

        public Builder setSlot(AvailabilitySlot slot) {
            this.slot = slot;
            return this;
        }


        public Builder copy(Booking booking) {
            this.bookingId = booking.bookingId;
            this.bookingDateTime = booking.bookingDateTime;
            this.status = booking.status;
            this.member = booking.member;
            this.slot = booking.slot;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
