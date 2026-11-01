/*
 * PasswordPolicySelector.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import org.springframework.stereotype.Component;
import za.ac.cput.domain.enums.RoleType;

import java.util.List;

/*
 * Chooses the password policy that applies to an account.
 * Members get the standard policy. Trainers and admins hold privileged
 * accounts, so they get the strict policy. An unknown role is treated as
 * privileged so that it can never get the weaker policy by mistake.
 */
@Component
public class PasswordPolicySelector {

    private final PasswordPolicy standardPolicy;
    private final PasswordPolicy strictPolicy;

    public PasswordPolicySelector(StandardPasswordPolicy standardPolicy,
                                  StrictPasswordPolicy strictPolicy) {
        this.standardPolicy = standardPolicy;
        this.strictPolicy = strictPolicy;
    }

    // Policy for a single role.
    public PasswordPolicy policyFor(RoleType role) {
        if (isPrivileged(role)) {
            return strictPolicy;
        }
        return standardPolicy;
    }

    // Policy for an account that holds more than one role.
    // The strongest policy wins, so a user who is both a member and an
    // admin is held to the admin rules.
    public PasswordPolicy policyFor(List<RoleType> roles) {
        if (roles == null || roles.isEmpty()) {
            return strictPolicy;
        }
        for (RoleType role : roles) {
            if (isPrivileged(role)) {
                return strictPolicy;
            }
        }
        return standardPolicy;
    }

    private boolean isPrivileged(RoleType role) {
        if (role == null) {
            return true;
        }
        return role == RoleType.TRAINER || role == RoleType.ADMIN;
    }
}
