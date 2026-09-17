package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Account;
import za.ac.cput.repository.IAccountRepository;
import za.ac.cput.service.IAccountService;
import za.ac.cput.util.Helper;

import java.time.LocalDate;
import java.util.List;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@Service
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository repository;

    @Autowired
    public AccountServiceImpl(IAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account create(Account account) {
        if (account == null) {
            return null;
        }
        if (Helper.isNullOrEmpty(account.getEmail()) || !Helper.isValidEmail(account.getEmail())) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        if (Helper.isNullOrEmpty(account.getPassword())) {
            throw new IllegalArgumentException("Password is required.");
        }
        String id = account.getAccountId();
        if (Helper.isNullOrEmpty(id)) {
            account = new Account.Builder()
                    .copy(account)
                    .setAccountId(Helper.generateId())
                    .build();
        }
        if (account.getRegistrationDate() == null) {
            account = new Account.Builder()
                    .copy(account)
                    .setRegistrationDate(LocalDate.now())
                    .build();
        }
        return repository.save(account);
    }

    @Override
    public Account read(String accountId) {
        if (accountId == null) {
            return null;
        }
        return repository.findById(accountId).orElse(null);
    }

    @Override
    public Account update(Account account) {
        if (account == null || account.getAccountId() == null) {
            return null;
        }
        if (!repository.existsById(account.getAccountId())) {
            return null;
        }
        return repository.save(account);
    }

    @Override
    public boolean delete(String accountId) {
        if (accountId == null || !repository.existsById(accountId)) {
            return false;
        }
        repository.deleteById(accountId);
        return true;
    }

    @Override
    public List<Account> getAll() {
        return repository.findAll();
    }

    @Override
    public Account findByEmail(String email) {
        if (email == null) {
            return null;
        }
        return repository.findByEmail(email);
    }

    @Override
    public List<Account> findAccountByRegistrationDate(LocalDate registrationDate) {
        if (registrationDate == null) {
            return List.of();
        }
        return repository.findAccountByRegistrationDate(registrationDate);
    }
}
