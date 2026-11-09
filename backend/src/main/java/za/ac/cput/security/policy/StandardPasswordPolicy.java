/*
 * StandardPasswordPolicy.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StandardPasswordPolicy implements PasswordPolicy {

    private static final int MINIMUM_LENGTH = 8;

    @Override
    public String name() {
        return "standard";
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
        if (!hasLetter(password)) {
            violations.add("Password must contain at least one letter.");
        }
        if (!hasDigit(password)) {
            violations.add("Password must contain at least one digit.");
        }

        return violations;
    }

    private boolean hasLetter(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
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
}
