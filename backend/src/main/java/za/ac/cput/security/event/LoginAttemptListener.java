package za.ac.cput.security.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoginAttemptListener {
    private final LoginAttemptTracker tracker;

    public LoginAttemptListener(LoginAttemptTracker tracker) {
        this.tracker = tracker;
    }

    @EventListener
    public void onSecurityEvent(SecurityEvent event) {
        switch (event.type()) {
            case LOGIN_FAILED -> tracker.recordFailure(event.subject());
            case LOGIN_SUCCESS, REGISTERED -> tracker.reset(event.subject());
            default -> { }
        }
    }
}
