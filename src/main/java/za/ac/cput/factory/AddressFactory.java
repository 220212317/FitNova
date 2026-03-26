package za.ac.cput.factory;

import za.ac.cput.entity.Address;

/**
 * AddressFactory.java
 * Factory class for creating Address objects
 * Author: Athi Sintiya (220212317)
 */
public class AddressFactory {

    public static Address createAddress(String addressId, String streetNumber,
                                        String streetName, String suburbName, String city,
                                        String postalCode, String province,
                                        String country) {

        if (addressId == null || addressId.isEmpty()) {
            return null;
        }

        if (streetNumber == null || streetNumber.isEmpty()) {
            return null;
        }

        if (streetName == null || streetName.isEmpty()) {
            return null;
        }

        if (suburbName == null || suburbName.isEmpty()) {
            return null;
        }

        if (city == null || city.isEmpty()) {
            return null;
        }

        if (postalCode == null || postalCode.isEmpty()) {
            return null;
        }

        if (province == null || province.isEmpty()) {
            return null;
        }

        if (country == null || country.isEmpty()) {
            return null;
        }


        return new Address.Builder()
                .setAddressId(addressId)
                .setStreetNumber(streetNumber)
                .setStreetName(streetName)
                .setSuburbName(suburbName)
                .setCity(city)
                .setPostalCode(postalCode)
                .setProvince(province)
                .setCountry(country)
                .build();
    }
}
