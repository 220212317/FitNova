package za.ac.cput.entity;
/**
 *
 * 25 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */
import za.ac.cput.entity.enums.RoleType;

import java.time.LocalDate;

public class UserAccount
{
    private String accountId;
    private String email;
    private String password;
    private RoleType role;
    private LocalDate registrationDate;

     private UserAccount(Builder builder){
         this.accountId = builder.accountId;
         this.email = builder.email;
         this.password = builder.password;
         this.role = builder.role;
         this.registrationDate = builder.registrationDate;

     }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public static class Builder {
        private String accountId;
        private String email;
        private String password;
        private RoleType role;
        private LocalDate registrationDate;


        public Builder accountId(String accountId){
            this.accountId = accountId;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public Builder password(String password){
            this.password= password;
            return this;
        }
        public Builder role(RoleType role){
            this.role = role;
            return this;
        }
        public Builder registrationDate(LocalDate registrationDate){
            this.registrationDate = registrationDate;
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "accountId='" + accountId + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", role=" + role +
                    ", registrationDate=" + registrationDate +
                    '}';
        }

        public UserAccount build(){

            return new UserAccount(this);
        }



    }
}
