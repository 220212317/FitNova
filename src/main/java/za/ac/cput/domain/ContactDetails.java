package za.ac.cput.domain;

public class ContactDetails {
    private String emails;
    private String cellphone;
    private String homephone;

    private ContactDetails(Builder builder) {
        this.emails = builder.emails;
        this.cellphone = builder.cellphone;
        this.homephone = builder.homephone;
    }

    public String getAllContactDetails() {
        return "Emails: " + emails + ", phone number: " +
                cellphone + ", homephone: " + homephone;
    }

    public void updateEmails(String newEmails) {
        if (isValidEmail(newEmails)) {
            this.emails = newEmails;
        } else {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    public void updateCellphone(String newCellphone) {
        if (isValidPhone(newCellphone)) {
            this.cellphone = newCellphone;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public void updateHomephone(String newHomephone) {
        if (isValidPhone(newHomephone)) {
            this.homephone = newHomephone;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9]{10}$");
    }

    private boolean isValidEmail(String emails) {
        return emails != null && emails.matches("@");
    }

    public String getEmails() {
        return emails;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getHomephone() {
        return homephone;
    }

    public static class Builder {
        private String emails;
        private String cellphone;
        private String homephone;

        public Builder setEmails(String emails) {
            this.emails = emails;
            return this;
        }

        public Builder setCellphone(String cellphone) {
            this.cellphone = cellphone;
            return this;
        }

        public Builder setHomephone(String homephone) {
            this.homephone = homephone;
            return this;
        }

        public ContactDetails build() {
            return new ContactDetails(this);
        }
    }
}
