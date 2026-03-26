package za.ac.cput.entity;

/*
ContactDetails.java
Entity for Contact Details
Author: Inga Plati (230126634)
Date: 25 March 2026
*/

public class ContactDetails {

    private String contactId;
    private String cellphoneNumber;
    private String emergencyContactNumber;
    private String emergencyContactName;

    private ContactDetails(Builder builder) {
        this.contactId = builder.contactId;
        this.cellphoneNumber = builder.cellphoneNumber;
        this.emergencyContactNumber = builder.emergencyContactNumber;
        this.emergencyContactName = builder.emergencyContactName;
    }

    public String getContactId() {
        return contactId;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    @Override
    public String toString() {
        return "ContactDetails{" +
                "contactId='" + contactId + '\'' +
                ", cellphoneNumber='" + cellphoneNumber + '\'' +
                ", emergencyContactNumber='" + emergencyContactNumber + '\'' +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                '}';
    }

    public static class Builder {

        private String contactId;
        private String cellphoneNumber;
        private String emergencyContactNumber;
        private String emergencyContactName;

        public Builder setContactId(String contactId) {
            this.contactId = contactId;
            return this;
        }

        public Builder setCellphoneNumber(String cellphoneNumber) {
            this.cellphoneNumber = cellphoneNumber;
            return this;
        }

        public Builder setEmergencyContactNumber(String emergencyContactNumber) {
            this.emergencyContactNumber = emergencyContactNumber;
            return this;
        }

        public Builder setEmergencyContactName(String emergencyContactName) {
            this.emergencyContactName = emergencyContactName;
            return this;
        }

        public ContactDetails build() {
            return new ContactDetails(this);
        }
    }
}