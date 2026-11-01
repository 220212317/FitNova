/*
 * PasswordPolicySelector.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import org.springframework.stereotype.Component;
import za.ac.cput.domain.enums.RoleType;

import java.util.List;

@Component
public class PasswordPolicySelector {

    private final PasswordPolicy standardPolicy;
    private final PasswordPolicy strictPolicy;

    public PasswordPolicySelector(StandardPasswordPolicy standardPolicy,
                                  StrictPasswordPolicy strictPolicy) {
        this.standardPolicy = standardPolicy;
        this.strictPolicy = strictPolicy;
    }

    public PasswordPolicy policyFor(RoleType role) {
        if (isPrivileged(role)) {
            return strictPolicy;
        }
        return standardPolicy;
    }

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
