/*
 * PasswordPolicy.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import java.util.List;

public interface PasswordPolicy {

    String name();

    List<String> violations(String password);

    default boolean isValid(String password) {
        return violations(password).isEmpty();
    }

    default void enforce(String password) {
        List<String> violations = violations(password);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password rejected by the " + name() + " password policy: "
                            + String.join(" ", violations));
        }
    }
}
