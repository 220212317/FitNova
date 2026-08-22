package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.service.IAvailabilitySlotService;

import java.util.List;

/*
 * Controller for AvailabilitySlot
 * Author: Phumelela Sakie (240040546)
 */

@RestController
@RequestMapping("/availability-slots")
public class AvailabilitySlotController {

    private final IAvailabilitySlotService service;

    public AvailabilitySlotController(IAvailabilitySlotService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<AvailabilitySlot> create(
            @RequestBody AvailabilitySlot availabilitySlot) {

        AvailabilitySlot created = service.create(availabilitySlot);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{slotId}")
    public ResponseEntity<AvailabilitySlot> read(
            @PathVariable String slotId) {

        AvailabilitySlot availabilitySlot = service.read(slotId);

        if (availabilitySlot == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(availabilitySlot, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AvailabilitySlot>> readAll() {

        List<AvailabilitySlot> availabilitySlots = service.readAll();

        return new ResponseEntity<>(availabilitySlots, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<AvailabilitySlot> update(
            @RequestBody AvailabilitySlot availabilitySlot) {

        AvailabilitySlot updated = service.update(availabilitySlot);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{slotId}")
    public ResponseEntity<Void> delete(
            @PathVariable String slotId) {

        boolean deleted = service.delete(slotId);

        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}