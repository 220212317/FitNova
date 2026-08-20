package za.ac.cput.service;

import za.ac.cput.domain.Account;

import java.time.LocalDate;
import java.util.List;

/*
 * Author: Athi Sintiya
 * 220212317
 */

public interface IAccountService extends IService<Account, String> {

    Account findByEmail(String email);

    List<Account> findAccountByRegistrationDate(LocalDate registrationDate);
}