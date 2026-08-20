package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Account;

public interface IAccountRepository extends JpaRepository<Account, String> {
}