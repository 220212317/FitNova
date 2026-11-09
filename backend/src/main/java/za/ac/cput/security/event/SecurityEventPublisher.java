package za.ac.cput.security.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

@Component
public class SecurityEventPublisher {
    private final ApplicationEventPublisher publisher;
    private final Clock clock;

    @Autowired
    public SecurityEventPublisher(ApplicationEventPublisher publisher) {
        this(publisher, Clock.systemUTC());
    }

    SecurityEventPublisher(ApplicationEventPublisher publisher, Clock clock) {
        this.publisher = Objects.requireNonNull(publisher);
        this.clock = Objects.requireNonNull(clock);
    }

    public SecurityEvent publish(SecurityEvent.Type type, String subject) {
        SecurityEvent event = new SecurityEvent(type, subject, Instant.now(clock));
        publish(event);
        return event;
    }

    public void publish(SecurityEvent event) {
        publisher.publishEvent(Objects.requireNonNull(event));
    }
}
