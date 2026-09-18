package za.ac.cput.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.service.IAvailabilitySlotService;

import java.util.List;
import java.util.Map;

/*

Controller for AvailabilitySlot
Author: Phumelela Sakie (240040546)
*/
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

        AvailabilitySlot created = service.create(availabilitySlot);

        if (created == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Could not create slot. Check date, start/end time, status, and that the trainer userId exists.")
            );
        }

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

    @GetMapping({"/all", "/getAll"})
    public ResponseEntity<List<AvailabilitySlot>> readAll() {
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody AvailabilitySlot availabilitySlot) {

        AvailabilitySlot updated = service.update(availabilitySlot);

        if (updated == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Could not update slot. Check that the slot exists, date, start/end time, status, and trainer userId are valid.")
            );
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{slotId}")
    public ResponseEntity<Void> delete(
            @PathVariable String slotId) {

        if (!service.delete(slotId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}