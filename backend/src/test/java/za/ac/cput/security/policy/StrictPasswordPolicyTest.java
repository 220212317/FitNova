/*
 * StrictPasswordPolicyTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrictPasswordPolicyTest {

    private final StrictPasswordPolicy policy = new StrictPasswordPolicy();

    @Test
    void acceptsStrongPassword() {
        assertTrue(policy.isValid("Str0ng!Pass"));
        assertTrue(policy.violations("Str0ng!Pass").isEmpty());
    }

    @Test
    void rejectsNullPassword() {
        assertFalse(policy.isValid(null));
        assertEquals(1, policy.violations(null).size());
    }

    @Test
    void rejectsPasswordShorterThanEightCharacters() {
        assertFalse(policy.isValid("Ab1!xyz"));
        assertThrows(IllegalArgumentException.class, () -> policy.enforce("Ab1!xyz"));
    }

    @Test
    void rejectsPasswordWithoutUpperCase() {
        assertFalse(policy.isValid("str0ng!pass"));
    }

    @Test
    void rejectsPasswordWithoutLowerCase() {
        assertFalse(policy.isValid("STR0NG!PASS"));
    }

    @Test
    void rejectsPasswordWithoutDigit() {
        assertFalse(policy.isValid("Strong!Pass"));
    }

    @Test
    void rejectsPasswordWithoutSymbol() {
        assertFalse(policy.isValid("Str0ngPass1"));
    }

    @Test
    void acceptsPasswordOfExactlyEightCharacters() {
        assertTrue(policy.isValid("Ab1!defg"));
    }

    @Test
    void rejectsPasswordAcceptedByStandardPolicy() {
        assertFalse(policy.isValid("password1"));
    }

    @Test
    void reportsPolicyName() {
        assertEquals("strict", policy.name());
    }
}
