package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Account;

import java.time.LocalDate;
import java.util.List;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@Repository
public interface IAccountRepository extends JpaRepository<Account, String> {

    Account findByEmail(String email);

    List<Account> findAccountByRegistrationDate(LocalDate registrationDate);
}