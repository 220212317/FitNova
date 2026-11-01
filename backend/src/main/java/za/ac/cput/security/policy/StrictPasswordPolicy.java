/*
 * StrictPasswordPolicy.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StrictPasswordPolicy implements PasswordPolicy {

    private static final int MINIMUM_LENGTH = 8;

    @Override
    public String name() {
        return "strict";
    }

    @Override
    public List<String> violations(String password) {
        List<String> violations = new ArrayList<>();

        if (password == null) {
            violations.add("Password is required.");
            return violations;
        }

        if (password.length() < MINIMUM_LENGTH) {
            violations.add("Password must be at least " + MINIMUM_LENGTH + " characters long.");
        }
        if (!hasUpperCase(password)) {
            violations.add("Password must contain at least one upper-case letter.");
        }
        if (!hasLowerCase(password)) {
            violations.add("Password must contain at least one lower-case letter.");
        }
        if (!hasDigit(password)) {
            violations.add("Password must contain at least one digit.");
        }
        if (!hasSymbol(password)) {
            violations.add("Password must contain at least one symbol, for example !@#$%.");
        }

        return violations;
    }

    private boolean hasUpperCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLowerCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDigit(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSymbol(String password) {
        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);
            if (!Character.isLetterOrDigit(character) && !Character.isWhitespace(character)) {
                return true;
            }
        }
        return false;
    }
}
