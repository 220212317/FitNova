package za.ac.cput.factory;
import za.ac.cput.domain.Contact;
import za.ac.cput.domain.User;
import za.ac.cput.util.Helper;
//Lisakhanya Tshokolo 22023921

public class ContactFactory {
    public static Contact createContact(/*User user*/ String cellphoneNumber, String alternativeCellphoneNumber, String emailAddress) {
       /*if(user == null){
           return null;
       }*/

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
                //.setUser(user)
                .setCellphoneNumber(cellphoneNumber)
                .setAlternativeCellphoneNumber(alternativeCellphoneNumber)
                .setEmailAddress(emailAddress)
                .build();
    }
}
