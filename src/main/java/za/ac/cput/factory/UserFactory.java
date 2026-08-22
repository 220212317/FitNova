package za.ac.cput.factory;

import za.ac.cput.domain.Account;
import za.ac.cput.domain.Address;
import za.ac.cput.domain.Contact;
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;
import za.ac.cput.util.Helper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * UserFactory.java
 * Author: Collins Shibambo
 * 230093183
 */
public class UserFactory {

    public static User createUser(String userId, String firstName, String lastName, LocalDate dateOfBirth,
                                  Account account, Demographic demographic, Address address,
                                  Contact contact, List<NextOfKinContact> nextOfKinContacts) {

        if (Helper.isNullOrEmpty(firstName)) {
            return null;
        }

        if (Helper.isNullOrEmpty(lastName)) {
            return null;
        }

        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            return null;
        }

        if (account == null) {
            return null;
        }

        if (demographic == null) {
            return null;
        }

        if (address == null) {
            return null;
        }

        if (contact == null) {
            return null;
        }

        if (nextOfKinContacts == null) {
            nextOfKinContacts = new ArrayList<>();
        }

        if (Helper.isNullOrEmpty(userId)) {
            userId = Helper.generateId();
        }

        List<NextOfKinContact> linkedNextOfKinContacts = new ArrayList<>();

        User user = new User.Builder()
                .setUserId(userId)
                .setFirstName(firstName.trim())
                .setLastName(lastName.trim())
                .setDateOfBirth(dateOfBirth)
                .setAccount(account)
                .setDemographic(demographic)
                .setAddress(address)
                .setContact(contact)
                .setNextOfKinContacts(linkedNextOfKinContacts)
                .build();

        for (NextOfKinContact nextOfKinContact : nextOfKinContacts) {
            if (nextOfKinContact == null) {
                continue;
            }
            linkedNextOfKinContacts.add(
                    new NextOfKinContact.Builder()
                            .copy(nextOfKinContact)
                            .setUser(user)
                            .build()
            );
        }

        return user;
    }

    public static User createUser(String firstName, String lastName, LocalDate dateOfBirth,
                                  Account account, Demographic demographic, Address address,
                                  Contact contact, List<NextOfKinContact> nextOfKinContacts) {
        return createUser(Helper.generateId(), firstName, lastName, dateOfBirth,
                account, demographic, address, contact, nextOfKinContacts);
    }
}