package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

/*
 * Account.java
 * Author: Athi Sintiya
 * 220212317
 */

@Entity
@Table(name = "account")
public class Account {

    @Id
    private String accountId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate registrationDate;

    @OneToOne(mappedBy = "account")
    @JsonIgnore
    private User user;

    protected Account() {
    }

    private Account(Builder builder) {
        this.accountId = builder.accountId;
        this.email = builder.email;
        this.password = builder.password;
        this.registrationDate = builder.registrationDate;
        this.user = builder.user;
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

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
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
        private User user;

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

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder copy(Account account) {
            this.accountId = account.accountId;
            this.email = account.email;
            this.password = account.password;
            this.registrationDate = account.registrationDate;
            this.user = account.user;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}