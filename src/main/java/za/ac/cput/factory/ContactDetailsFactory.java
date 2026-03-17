package za.ac.cput.factory;

import za.ac.cput.domain.ContactDetails;

public class ContactDetailsFactory {
    public static ContactDetails createContactDetails(String email, String cellphone, String homeNumber){
        if(email.isEmpty() || email == null)
            return null;
        if(cellphone.isEmpty() || cellphone == null)
            return null;
        if(homeNumber.isEmpty() || homeNumber == null)
            return null;

        return new ContactDetails.Builder()
                .setEmails(email)
                .setCellphone(cellphone)
                .setHomephone(homeNumber)
                .build();

    }
}
