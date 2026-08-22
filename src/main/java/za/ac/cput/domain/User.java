/* User.java
     User POJO class (Entity)
     Author: Collins Shibambo (230093183)
     Date: 18 August 2026 */
package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "demographic_id", referencedColumnName = "demography_id")
    private Demographic demographic;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id", referencedColumnName = "contactId")
    private Contact contact;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NextOfKinContact> nextOfKinContacts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "trainer")
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles = new ArrayList<>();

    public User() {

    }

    private User(Builder builder) {
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.dateOfBirth = builder.dateOfBirth;
        this.account = builder.account;
        this.demographic = builder.demographic;
        this.address = builder.address;
        this.contact = builder.contact;
        this.nextOfKinContacts = builder.nextOfKinContacts;
        this.bookings = builder.bookings;
        this.availabilitySlots = builder.availabilitySlots;
        this.userRoles = builder.userRoles;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Account getAccount() {
        return account;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public Address getAddress() {
        return address;
    }

    public Contact getContact() {
        return contact;
    }

    public List<NextOfKinContact> getNextOfKinContacts() {
        return nextOfKinContacts;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public static class Builder {
        private String userId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private Account account;
        private Demographic demographic;
        private Address address;
        private Contact contact;
        private List<NextOfKinContact> nextOfKinContacts = new ArrayList<>();
        private List<Booking> bookings = new ArrayList<>();
        private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();
        private List<UserRole> userRoles = new ArrayList<>();

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public Builder setDemographic(Demographic demographic) {
            this.demographic = demographic;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder setContact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public Builder setNextOfKinContacts(List<NextOfKinContact> nextOfKinContacts) {
            this.nextOfKinContacts = nextOfKinContacts;
            return this;
        }

        public Builder setBookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }

        public Builder setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
            this.availabilitySlots = availabilitySlots;
            return this;
        }

        public Builder setUserRoles(List<UserRole> userRoles) {
            this.userRoles = userRoles;
            return this;
        }

        public Builder copy(User user) {
            this.userId = user.userId;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.dateOfBirth = user.dateOfBirth;
            this.account = user.account;
            this.demographic = user.demographic;
            this.address = user.address;
            this.contact = user.contact;
            this.nextOfKinContacts = user.nextOfKinContacts;
            this.bookings = user.bookings;
            this.availabilitySlots = user.availabilitySlots;
            this.userRoles = user.userRoles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}