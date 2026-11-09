package za.ac.cput.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.security.adapter.AuthenticatedUser;

import java.util.Map;
import java.util.NoSuchElementException;

/** The signed-in user's own profile. The user id always comes from the token, never from the URL. */
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> get(@AuthenticationPrincipal AuthenticatedUser me) {
        return ResponseEntity.ok(profileService.get(me.getUserId()));
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> update(@AuthenticationPrincipal AuthenticatedUser me,
                                                  @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.update(me.getUserId(), request));
    }

    /** Body: { "password": "..." } — the password is required to confirm deletion. */
    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser me,
                                       @RequestBody Map<String, String> body) {
        profileService.delete(me.getUserId(), body == null ? null : body.get("password"));
        return ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> notFound(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(Map.of("status", 404, "error", "Not Found", "message", ex.getMessage()));
    }
}
