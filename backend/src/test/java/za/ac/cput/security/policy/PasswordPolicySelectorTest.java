/*
 * PasswordPolicySelectorTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.enums.RoleType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasswordPolicySelectorTest {

    private final StandardPasswordPolicy standardPolicy = new StandardPasswordPolicy();
    private final StrictPasswordPolicy strictPolicy = new StrictPasswordPolicy();
    private final PasswordPolicySelector selector =
            new PasswordPolicySelector(standardPolicy, strictPolicy);

    @Test
    void returnsStandardPolicyForMember() {
        assertSame(standardPolicy, selector.policyFor(RoleType.MEMBER));
    }

    @Test
    void returnsStrictPolicyForTrainer() {
        assertSame(strictPolicy, selector.policyFor(RoleType.TRAINER));
    }

    @Test
    void returnsStrictPolicyForAdmin() {
        assertSame(strictPolicy, selector.policyFor(RoleType.ADMIN));
    }

    @Test
    void returnsStrictPolicyForNullRole() {
        assertSame(strictPolicy, selector.policyFor((RoleType) null));
    }

    @Test
    void returnsStandardPolicyWhenAllRolesAreMembers() {
        List<RoleType> roles = new ArrayList<>();
        roles.add(RoleType.MEMBER);
        assertSame(standardPolicy, selector.policyFor(roles));
    }

    @Test
    void returnsStrictPolicyWhenAnyRoleIsPrivileged() {
        List<RoleType> roles = new ArrayList<>();
        roles.add(RoleType.MEMBER);
        roles.add(RoleType.ADMIN);
        assertSame(strictPolicy, selector.policyFor(roles));
    }

    @Test
    void returnsStrictPolicyForEmptyRoles() {
        assertSame(strictPolicy, selector.policyFor(new ArrayList<RoleType>()));
    }

    @Test
    void returnsStrictPolicyForNullRoles() {
        assertSame(strictPolicy, selector.policyFor((List<RoleType>) null));
    }
}
