/*
 * PasswordPolicy.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import java.util.List;

/*
 * Strategy interface for password rules.
 * PasswordPolicySelector decides which policy applies to an account.
 */
public interface PasswordPolicy {

    // Policy name, used in error messages.
    String name();

    // Returns every rule the password breaks. An empty list means the
    // password is acceptable.
    List<String> violations(String password);

    // True when the password breaks no rule.
    default boolean isValid(String password) {
        return violations(password).isEmpty();
    }

    // Validates the password and throws when it is not acceptable.
    // GlobalExceptionHandler turns IllegalArgumentException into HTTP 400
    // and puts the message in the response body.
    default void enforce(String password) {
        List<String> violations = violations(password);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password rejected by the " + name() + " password policy: "
                            + String.join(" ", violations));
        }
    }
}
