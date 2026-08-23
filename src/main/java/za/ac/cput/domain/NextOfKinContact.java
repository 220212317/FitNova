package za.ac.cput.domain;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
//Lisakhanya Tshokolo 220239215

@Entity
@Table(name = "next_of_kin_contact")
public class NextOfKinContact {

    @Id
    private String nextOfKinContactId;

    private String firstName;
    private String lastName;
    private String relationship;
    private String cellphoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "userId")
    @JsonIgnore
    private User user;

    protected NextOfKinContact() {
    }

    public NextOfKinContact(Builder builder) {
        this.nextOfKinContactId = builder.nextOfKinContactId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.relationship = builder.relationship;
        this.cellphoneNumber = builder.cellphoneNumber;
        this.user = builder.user;
    }

    public String getNextOfKinContactId() {

        return nextOfKinContactId;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public String getRelationship() {

        return relationship;
    }

    public String getCellphoneNumber() {

        return cellphoneNumber;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "NextOfKinContact{" +
                "nextOfKinContactId='" + nextOfKinContactId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", relationship='" + relationship + '\'' +
                ", cellphoneNumber='" + cellphoneNumber + '\'' +
                ", user=" + user +
                '}';
    }

    public static class Builder {
        private String nextOfKinContactId;
        private String firstName;
        private String lastName;
        private String relationship;
        private String cellphoneNumber;
        private User user;

        public Builder setNextOfKinContactId(String nextOfKinContactId) {
            this.nextOfKinContactId = nextOfKinContactId;
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

        public Builder setRelationship(String relationship) {
            this.relationship = relationship;
            return this;
        }

        public Builder setCellphoneNumber(String cellphoneNumber) {
            this.cellphoneNumber = cellphoneNumber;
            return this;
        }
        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder copy(za.ac.cput.domain.NextOfKinContact nextOfKinContact) {
            this.nextOfKinContactId = nextOfKinContact.nextOfKinContactId;
            this.firstName = nextOfKinContact.firstName;
            this.lastName = nextOfKinContact.lastName;
            this.relationship = nextOfKinContact.relationship;
            this.cellphoneNumber = nextOfKinContact.cellphoneNumber;
            this.user = nextOfKinContact.user;
            return this;
        }

        public za.ac.cput.domain.NextOfKinContact build() {
            return new za.ac.cput.domain.NextOfKinContact(this);
        }
    }
}