package za.ac.cput.util;

import java.util.UUID;
import java.util.regex.Pattern;

/*
 * Author: Athi Sintiya
 * 220212317
 */
public class Helper {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}