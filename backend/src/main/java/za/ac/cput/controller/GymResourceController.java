package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.GymResource;
import za.ac.cput.service.IGymResourceService;

import java.util.List;

/**
 * Gym resources. Reading needs any signed-in user; create / update / delete need the
 * resource:manage authority (admins) - see SecurityConfig.
 */
@RestController
@RequestMapping("/resource")
public class GymResourceController {

    private final IGymResourceService service;

    @Autowired
    public GymResourceController(IGymResourceService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<GymResource> create(@RequestBody GymResource resource) {
        GymResource created = service.create(resource);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{resourceId}")
    public ResponseEntity<GymResource> read(@PathVariable String resourceId) {
        GymResource resource = service.read(resourceId);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/update")
    public ResponseEntity<GymResource> update(@RequestBody GymResource resource) {
        GymResource updated = service.update(resource);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{resourceId}")
    public ResponseEntity<Void> delete(@PathVariable String resourceId) {
        if (!service.delete(resourceId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GymResource>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
