package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
/*
 * Avuyile Sitoyi
 * 240971051
 *
 * */
@Repository
public interface IBookingRepository extends JpaRepository<Booking,String> {
    List<Booking> findByMember_UserId(String userId);
    List<Booking> findBySlot_SlotId(String slotId);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByMember_UserIdAndStatus(String userId, BookingStatus status);
    List<Booking> findByBookingDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
