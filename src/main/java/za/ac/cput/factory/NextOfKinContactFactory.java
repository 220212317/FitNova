package za.ac.cput.factory;

import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.util.Helper;

public class NextOfKinContactFactory {
    public static NextOfKinContact createNextOfKinContact(
            String firstName, String lastName, String relationship, String cellphoneNumber) {

        if(Helper.isEmptyorNull(firstName)
                || Helper.isEmptyorNull(lastName)
                || Helper.isEmptyorNull(cellphoneNumber)) {
            return null;
        }

        String nextofkinContactId = Helper.generateUUID();

        return new NextOfKinContact.Builder()
                .setNextOfKinContactId(nextofkinContactId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setRelationship(relationship)
                .setCellphoneNumber(cellphoneNumber)
                .build();
    }
}
