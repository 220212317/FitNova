package za.ac.cput.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
//Lisakhanya Tshokolo 220239215

@Entity
public class Contact {
    @Id
    private String contactId;

    private String cellphoneNumber;
    private String alternativeCellphoneNumber;
    private String emailAddress;

    protected Contact() {}

    public Contact(Builder builder) {
        this.contactId = builder.contactId;
        this.cellphoneNumber = builder.cellphoneNumber;
        this.alternativeCellphoneNumber = builder.alternativeCellphoneNumber;
        this.emailAddress = builder.emailAddress;
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

    public static class Builder{
        private String contactId;
        private String cellphoneNumber;
        private String alternativeCellphoneNumber;
        private String emailAddress;

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

        public Builder copy(Contact contact){
            this.contactId = contact.contactId;
            this.cellphoneNumber = contact.cellphoneNumber;
            this.alternativeCellphoneNumber = contact.alternativeCellphoneNumber;
            this.emailAddress = contact.emailAddress;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }

    }
}
