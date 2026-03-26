package za.ac.cput.entity;
/**
 *
 * 25 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */
import java.time.LocalDate;

public class UserProfile
{
    private String profileId;
    private String firstName;
    private String lastName;
    private String gender ;
    private LocalDate dateOfBirth;


    private UserProfile(Builder builder){
       this.profileId = builder.profileId;
       this.firstName = builder.firstName;
       this.lastName = builder.lastName;
       this.gender = builder.gender;
       this.dateOfBirth = builder.dateOfBirth;

    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public static class Builder {
        private String profileId;
        private String firstName;
        private String lastName;
        private String gender ;
        private LocalDate dateOfBirth;


        public Builder profileId(String profileId){
            this.profileId = profileId;
            return this;

        }


        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;

        }


        public Builder lastName(String lastName){
            this.lastName = lastName;
            return this;

        }

        public Builder gender(String gender){
            this.gender = gender;
            return this;

        }

        public Builder dateOfBirth(LocalDate dateOfBirth){
            this.dateOfBirth = dateOfBirth;
            return this;

        }

        @Override
        public String toString() {
            return "Builder{" +
                    "profileId='" + profileId + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", gender='" + gender + '\'' +
                    ", dateOfBirth=" + dateOfBirth +
                    '}';
        }

        public UserProfile build(){
            return new UserProfile(this);
         }




    }
}


