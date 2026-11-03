package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.service.IAvailabilitySlotService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/availability-slots")
public class AvailabilitySlotController {

    private final IAvailabilitySlotService service;

    public AvailabilitySlotController(IAvailabilitySlotService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody AvailabilitySlot availabilitySlot) {

        try {
            AvailabilitySlot created = service.create(availabilitySlot);

            if (created == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("message",
                                "Could not create slot. Check the date, time range and availability status. The slot may also overlap an existing slot.")
                );
            }

            return new ResponseEntity<>(created, HttpStatus.CREATED);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/read/{slotId}")
    public ResponseEntity<AvailabilitySlot> read(
            @PathVariable String slotId) {

        AvailabilitySlot slot = service.read(slotId);

        if (slot == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(slot);
    }

    @GetMapping({"/all", "/getAll"})
    public ResponseEntity<List<AvailabilitySlot>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody AvailabilitySlot availabilitySlot) {

        try {
            AvailabilitySlot updated = service.update(availabilitySlot);

            if (updated == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("message",
                                "Could not update slot. Check its details and make sure it has no confirmed or completed bookings.")
                );
            }

            return ResponseEntity.ok(updated);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{slotId}")
    public ResponseEntity<?> delete(@PathVariable String slotId) {

        try {
            if (!service.delete(slotId)) {
                return ResponseEntity.badRequest().body(
                        Map.of("message",
                                "Could not delete slot. It may not exist or may have a confirmed or completed booking.")
                );
            }

            return ResponseEntity.noContent().build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}