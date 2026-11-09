package za.ac.cput.security.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * AuditLogListenerTest.java
 * Author: Avuyile Sitoyi (240971051)
 */
class AuditLogListenerTest {
    @Test
    void logsEveryEventType() {
        List<SecurityEvent> audited = new ArrayList<>();
        AuditLogListener listener = new AuditLogListener(audited::add);
        Instant at = Instant.parse("2026-11-09T12:00:00Z");

        for (SecurityEvent.Type type : SecurityEvent.Type.values()) {
            listener.onSecurityEvent(new SecurityEvent(type, "subject", at));
        }

        assertEquals(List.of(SecurityEvent.Type.values()), audited.stream().map(SecurityEvent::type).toList());
    }
}
