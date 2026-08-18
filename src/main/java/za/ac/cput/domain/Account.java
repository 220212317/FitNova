package za.ac.cput.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@Entity
@Table(name = "account")
public class Account {

    @Id
    private String accountId;
    private String email;
    private String password;
    private LocalDate registrationDate;

    protected Account() {
    }

    private Account(Builder builder) {
        this.accountId = builder.accountId;
        this.email = builder.email;
        this.password = builder.password;
        this.registrationDate = builder.registrationDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }



    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }

    public static class Builder {
        private String accountId;
        private String email;
        private String password;
        private LocalDate registrationDate;

        public Builder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRegistrationDate(LocalDate registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder copy(Account account) {
            this.accountId = account.getAccountId();
            this.email = account.getEmail();
            this.password = account.getPassword();
            this.registrationDate = account.getRegistrationDate();
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}