package za.ac.cput.factory;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;
import za.ac.cput.util.Helper;
//Lisakhanya Tshokolo 220239215

public class NextOfKinContactFactory {
    public static NextOfKinContact createNextOfKinContact(
            String firstName, String lastName, String relationship, String cellphoneNumber,User user) {

        /*if (user == null) {
            return null;
        }*/

        if (Helper.isNullOrEmpty(firstName)) {
                return null;
        }

        if (Helper.isNullOrEmpty(lastName)) {
                return null;
        }

        if (Helper.isNullOrEmpty(relationship)) {
                return null;
        }
        if (Helper.isNullOrEmpty(cellphoneNumber)) {
                return null;
        }

        if (user == null) {
            return null;
        }

        String nextOfKinContactId = Helper.generateId();

        return new NextOfKinContact.Builder()
                .setNextOfKinContactId(nextOfKinContactId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setRelationship(relationship)
                .setCellphoneNumber(cellphoneNumber)
                .setUser(user)
                .build();
    }
}
