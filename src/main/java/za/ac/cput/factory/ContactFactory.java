package za.ac.cput.factory;
import za.ac.cput.domain.Contact;
import za.ac.cput.util.Helper;
//Lisakhanya Tshokolo 22023921

public class ContactFactory {
    public static Contact createContact(String cellphoneNumber, String alternativeCellphoneNumber, String emailAddress) {
        if(Helper.isNullOrEmpty(cellphoneNumber) ){
            return null;
        }

        if(Helper.isNullOrEmpty(emailAddress)){
            return null;
        }

        if(!Helper.isValidEmail(emailAddress)){
            return null;
        }

        String contactId = Helper.generateId();

        return new Contact.Builder()
                    .setContactId(contactId)
                    .setCellphoneNumber(cellphoneNumber)
                    .setAlternativeCellphoneNumber(alternativeCellphoneNumber)
                    .setEmailAddress(emailAddress)
                    .build();
    }
}
