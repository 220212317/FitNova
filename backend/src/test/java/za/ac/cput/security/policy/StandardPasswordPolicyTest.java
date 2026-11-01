/*
 * StandardPasswordPolicyTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.policy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardPasswordPolicyTest {

    private final StandardPasswordPolicy policy = new StandardPasswordPolicy();

    @Test
    void acceptsPasswordWithLetterAndDigit() {
        assertTrue(policy.isValid("password1"));
        assertTrue(policy.violations("password1").isEmpty());
    }

    @Test
    void rejectsNullPassword() {
        assertFalse(policy.isValid(null));
        assertEquals(1, policy.violations(null).size());
    }

    @Test
    void rejectsPasswordShorterThanEightCharacters() {
        assertFalse(policy.isValid("pass1"));
        assertThrows(IllegalArgumentException.class, () -> policy.enforce("pass1"));
    }

    @Test
    void rejectsPasswordWithoutDigit() {
        assertFalse(policy.isValid("passwordonly"));
    }

    @Test
    void rejectsPasswordWithoutLetter() {
        assertFalse(policy.isValid("12345678"));
    }

    @Test
    void acceptsPasswordOfExactlyEightCharacters() {
        assertTrue(policy.isValid("passwor1"));
    }

    @Test
    void reportsPolicyName() {
        assertEquals("standard", policy.name());
    }
}
