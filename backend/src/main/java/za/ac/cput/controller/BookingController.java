package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.security.adapter.AuthenticatedUser;
import za.ac.cput.service.IBookingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final IBookingService bookingService;

    @Autowired
    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Booking booking) {
        Booking created = bookingService.create(booking);
        if (created == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message",
                    "Could not create booking. Check the member, available slot, status, and booking date."
            ));
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
    public ResponseEntity<?> update(@RequestBody Booking booking) {
        Booking updated = bookingService.update(booking);
        if (updated == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message",
                    "Could not update booking. Confirm it exists and has a valid status."
            ));
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
        return ResponseEntity.ok(bookingService.getBookingsByMember(userId));
    }

    @GetMapping("/findBySlot/{slotId}")
    public ResponseEntity<List<Booking>> findBySlot(@PathVariable String slotId) {
        return ResponseEntity.ok(bookingService.getBookingsBySlot(slotId));
    }

    @GetMapping("/findByStatus/{status}")
    public ResponseEntity<List<Booking>> findByStatus(
            @PathVariable BookingStatus status
    ) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<Booking>> findByTrainer(
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                bookingService.getBookingsByTrainer(currentUser.getUserId())
        );
    }

    @PostMapping("/{bookingId}/start")
    public ResponseEntity<?> startSession(
            @PathVariable String bookingId,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Booking booking = bookingService.startSession(
                    bookingId,
                    currentUser.getUserId()
            );

            if (booking == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message",
                        "Booking not found."
                ));
            }

            return ResponseEntity.ok(booking);
        } catch (IllegalStateException exception) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message",
                    exception.getMessage()
            ));
        }
    }

    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<?> completeSession(
            @PathVariable String bookingId,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Booking booking = bookingService.completeSession(
                    bookingId,
                    currentUser.getUserId()
            );

            if (booking == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message",
                        "Booking not found."
                ));
            }

            return ResponseEntity.ok(booking);
        } catch (IllegalStateException exception) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message",
                    exception.getMessage()
            ));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Booking>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    }
}