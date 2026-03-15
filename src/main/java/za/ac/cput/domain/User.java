package za.ac.cput.domain;

public class User {
    private String userID;
    private String name;
    private UserRole role;
    private Address address;
    private ContactDetails contactdetails;

    public User() {

    }

    private User(Builder builder) {
        this.userID = builder.userID;
        this.name = builder.name;
        this.role = builder.role;
        this.address = builder.address;
        this.contactdetails = builder.contactdetails;
    }

    public String getContactInfo() {
        return contactdetails.getAllContactDetails();
    }

    public boolean canSubmitReport() {
        return this.role == UserRole.USER;
    }

    public boolean cnaRequestPickUp() {
        return this.role == UserRole.USER || this.role == UserRole.COLLECTOR;
    }

    public boolean canReceiveNotification() {
        return true;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }

    public ContactDetails getContactdetails() {
        return contactdetails;
    }

    public static class Builder {
        private String userID;
        private String name;
        private UserRole role;
        private Address address;
        private ContactDetails contactdetails;

        public Builder userID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder contactdetails(ContactDetails contactdetails) {
            this.contactdetails = contactdetails;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}
