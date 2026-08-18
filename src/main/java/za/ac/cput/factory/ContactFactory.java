package za.ac.cput.factory;

import za.ac.cput.domain.Contact;
import za.ac.cput.util.Helper;

public class ContactFactory {
    public static Contact createContact(String cellphoneNumber, String alternativeCellphoneNumber,String emailAddress) {
        if(Helper.isEmptyorNull(cellphoneNumber) || Helper.isEmptyorNull(alternativeCellphoneNumber)) {
            return null;
        }
       /* if(!Helper.IsValidEmail(emailAddress)){
            return null;
        }*/
        String contactId = Helper.generateUUID();
        return new Contact.Builder()
                .setContactId(contactId)
                .setCellphoneNumber(cellphoneNumber)
                .setAlternativeCellphoneNumber(alternativeCellphoneNumber)
                .setEmailAddress(emailAddress)
                .build();
    }
}
