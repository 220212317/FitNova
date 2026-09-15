package za.ac.cput.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Returns the real error message to the frontend instead of a bare
 * "Internal Server Error", which makes profile-create failures diagnosable.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String detail = rootMessage(ex);
        String message;
        if (detail != null && detail.toLowerCase().contains("email")) {
            message = "An account with this email already exists. Use a different email.";
        } else if (detail != null && detail.toLowerCase().contains("duplicate")) {
            message = "Duplicate data rejected by the database: " + detail;
        } else if (detail != null && detail.toLowerCase().contains("foreign key")) {
            message = "Cannot complete operation: related records still reference this data.";
        } else {
            message = "Database constraint failed: " + detail;
        }
        return body(HttpStatus.CONFLICT, message, detail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return body(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        ex.printStackTrace();
        String detail = rootMessage(ex);
        return body(HttpStatus.INTERNAL_SERVER_ERROR,
                detail != null ? detail : ex.getMessage(), detail);
    }

    private static ResponseEntity<Map<String, Object>> body(HttpStatus status, String message, String detail) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (detail != null && !detail.equals(message)) {
            body.put("detail", detail);
        }
        return ResponseEntity.status(status).body(body);
    }

    private static String rootMessage(Throwable ex) {
        Throwable t = ex;
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        return t.getMessage();
    }
}
