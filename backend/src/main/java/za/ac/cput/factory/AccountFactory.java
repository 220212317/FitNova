package za.ac.cput.factory;

import za.ac.cput.domain.Account;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

/*
 * AccountFactory.java
 * Author: Athi Sintiya
 * 220212317
 */
public class AccountFactory {

    public static Account createAccount(String accountId, String email, String password, LocalDate registrationDate) {
        if (Helper.isNullOrEmpty(email) || !Helper.isValidEmail(email) || Helper.isNullOrEmpty(password)) {
            return null;
        }

        if (Helper.isNullOrEmpty(accountId)) {
            accountId = Helper.generateId();
        }

        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }

        return new Account.Builder()
                .setAccountId(accountId)
                .setEmail(email.trim())
                .setPassword(password)
                .setRegistrationDate(registrationDate)
                .build();
    }

    public static Account createAccount(String email, String password, LocalDate registrationDate) {
        return createAccount(Helper.generateId(), email, password, registrationDate);
    }

    public static Account createAccount(String email, String password) {
        return createAccount(Helper.generateId(), email, password, LocalDate.now());
    }
}