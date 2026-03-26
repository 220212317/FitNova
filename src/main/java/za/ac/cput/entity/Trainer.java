package za.ac.cput.entity;
/**
 *
 * 25 March 2026
 * Author: Lisakhanya Tshokolo
 * (220239215)
 */
public class Trainer {
    private String trainerId;
    private UserAccount account;
    private UserProfile profile;
    private  ContactDetails contactDetails;

    private Trainer(Builder builder) {
        this.trainerId = builder.trainerId;
        this.account = builder.account;
        this.profile = builder.profile;
        this.contactDetails = builder.contactDetails;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    public static class Builder{
        private String trainerId;
        private UserAccount account;
        private UserProfile profile;
        private ContactDetails contactDetails;
        public Builder setTrainerId(String trainerId) {
            this.trainerId = trainerId;
            return this;
        }
        public Builder setAccount(UserAccount account) {
            this.account = account;
            return this;
        }
        public Builder setProfile(UserProfile profile) {
            this.profile = profile;
            return this;
        }
        public Builder setContactDetails(ContactDetails contactDetails) {
            this.contactDetails = contactDetails;
            return this;
        }
        public Trainer build(){
            return new Trainer(this);
        }
    }
}
