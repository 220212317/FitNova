package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.service.IBookingService;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final IBookingService bookingService;

    @Autowired
    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        Booking created = bookingService.create(booking);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{bookingId}")
    public ResponseEntity<Booking> read(@PathVariable String bookingId) {
        Booking booking = bookingService.read(bookingId);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/update")
    public ResponseEntity<Booking> update(@RequestBody Booking booking) {
        Booking updated = bookingService.update(booking);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity<Void> delete(@PathVariable String bookingId) {
        boolean deleted = bookingService.delete(bookingId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByMember/{userId}")
    public ResponseEntity<List<Booking>> findByMember(@PathVariable String userId) {
        List<Booking> bookings = bookingService.getBookingsByMember(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/findBySlot/{slotId}")
    public ResponseEntity<List<Booking>> findBySlot(@PathVariable String slotId) {
        List<Booking> bookings = bookingService.getBookingsBySlot(slotId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/findByStatus/{status}")
    public ResponseEntity<List<Booking>> findByStatus(@PathVariable BookingStatus status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Booking>> getAll() {
        List<Booking> bookings = bookingService.getAll();
        return ResponseEntity.ok(bookings);
    }
}
