package za.ac.cput.factory;

import za.ac.cput.entity.ContactDetails;

import za.ac.cput.util.helper;

/*
ContactDetails.java
Factory for Contact Details
Author: Inga Plati (230126634)
Date: 25 March 2026
*/

public class ContactDetailsFactory {

    public static ContactDetails createContactDetails(
            String contactId,
            String cellphoneNumber,
            String emergencyContactNumber,
            String emergencyContactName) {


        if (helper.isNullOrEmpty(contactId) ||
                helper.isNullOrEmpty(cellphoneNumber) ||
                helper.isNullOrEmpty(emergencyContactNumber) ||
                helper.isNullOrEmpty(emergencyContactName)) {

            System.out.println("Invalid input: No field can be null or empty.");
            return null;
        }

        return new ContactDetails.Builder()
                .setContactId(contactId)
                .setCellphoneNumber(cellphoneNumber)
                .setEmergencyContactNumber(emergencyContactNumber)
                .setEmergencyContactName(emergencyContactName)
                .build();
    }
}

