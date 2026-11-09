package za.ac.cput.security.event;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * SecurityEventPublisherTest.java
 * Author: Avuyile Sitoyi (240971051)
 */
class SecurityEventPublisherTest {
    @Test
    void publishesEventToListeners() {
        AtomicReference<Object> published = new AtomicReference<>();
        ApplicationEventPublisher springPublisher = published::set;
        Instant now = Instant.parse("2026-11-09T12:00:00Z");
        SecurityEventPublisher publisher = new SecurityEventPublisher(
                springPublisher, Clock.fixed(now, ZoneOffset.UTC));

        SecurityEvent event = publisher.publish(SecurityEvent.Type.LOGIN_SUCCESS, "member@fitnova.test");

        assertEquals(SecurityEvent.Type.LOGIN_SUCCESS, event.type());
        assertEquals("member@fitnova.test", event.subject());
        assertEquals(now, event.at());
        assertEquals(event, published.get());
    }
}
