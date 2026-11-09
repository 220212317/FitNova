package za.ac.cput.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

@Component
public class AuditLogListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogListener.class);
    private final Consumer<SecurityEvent> auditSink;

    public AuditLogListener() {
        this(event -> LOGGER.info("security_event type={} subject={} at={}",
                event.type(), event.subject(), event.at()));
    }

    AuditLogListener(Consumer<SecurityEvent> auditSink) {
        this.auditSink = Objects.requireNonNull(auditSink);
    }

    @EventListener
    public void onSecurityEvent(SecurityEvent event) {
        auditSink.accept(Objects.requireNonNull(event));
    }
}
