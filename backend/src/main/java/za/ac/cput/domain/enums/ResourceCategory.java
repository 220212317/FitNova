package za.ac.cput.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Category of a gym resource. Serialised as lower-case ("manual", "nutrition", "tip")
 * so it matches what the React app already uses.
 */
public enum ResourceCategory {
    MANUAL,
    NUTRITION,
    TIP;

    @JsonValue
    public String toJson() {
        return name().toLowerCase(Locale.ROOT);
    }

    /** Unknown values become null so the service can reject them with a clear message. */
    @JsonCreator
    public static ResourceCategory fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
