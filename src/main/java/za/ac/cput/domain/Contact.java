package za.ac.cput.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
//Lisakhanya Tshokolo 220239215

@Entity
public class Contact {
    @Id
    private String contactId;

    private String cellphoneNumber;
    private String alternativeCellphoneNumber;
    private String emailAddress;

   /* @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId",nullable = false, unique = true)
    private User user;*/

    protected Contact() {
    }

    public Contact(Builder builder) {
        this.contactId = builder.contactId;
        this.cellphoneNumber = builder.cellphoneNumber;
        this.alternativeCellphoneNumber = builder.alternativeCellphoneNumber;
        this.emailAddress = builder.emailAddress;
        //this.user = builder.user;
    }

    public String getContactId() {
        return contactId;
    }

    public String getCellphoneNumber() {

        return cellphoneNumber;
    }

    public String getAlternativeCellphoneNumber() {

        return alternativeCellphoneNumber;
    }

    public String getEmailAddress() {

        return emailAddress;
    }

   /* public User getUser() {
        return user;
    }*/

    public static class Builder {
        private String contactId;
        private String cellphoneNumber;
        private String alternativeCellphoneNumber;
        private String emailAddress;
       // private User user;

        public Builder setContactId(String contactId) {
            this.contactId = contactId;
            return this;
        }

        public Builder setCellphoneNumber(String cellphoneNumber) {
            this.cellphoneNumber = cellphoneNumber;
            return this;
        }

        public Builder setAlternativeCellphoneNumber(String alternativeCellphoneNumber) {
            this.alternativeCellphoneNumber = alternativeCellphoneNumber;
            return this;
        }

        public Builder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

       /* public Builder setUser(User user) {
            this.user = user;
            return this;
        }*/

        public Builder copy(za.ac.cput.domain.Contact contact) {
            this.contactId = contact.contactId;
            this.cellphoneNumber = contact.cellphoneNumber;
            this.alternativeCellphoneNumber = contact.alternativeCellphoneNumber;
            this.emailAddress = contact.emailAddress;
            //this.user = contact.user;
            return this;
        }

        public za.ac.cput.domain.Contact build() {

            return new za.ac.cput.domain.Contact(this);
        }

    }
}
