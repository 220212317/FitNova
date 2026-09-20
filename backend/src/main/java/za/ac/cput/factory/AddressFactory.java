package za.ac.cput.factory;

import za.ac.cput.domain.Address;
import za.ac.cput.util.Helper;

/*
 * AddressFactory.java
 * Author: Athi Sintiya
 * 220212317
 */
public class AddressFactory {


    public static Address createAddress(String addressId,
                                        String streetNumber,
                                        String streetName,
                                        String suburbName,
                                        String city,
                                        String postalCode,
                                        String province,
                                        String country) {


        if (Helper.isNullOrEmpty(streetName) ||
                Helper.isNullOrEmpty(city) ||
                Helper.isNullOrEmpty(country)) {
            return null;
        }

        if (Helper.isNullOrEmpty(addressId)) {
            addressId = Helper.generateId();
        }

        return new Address.Builder()
                .setAddressId(addressId)
                .setStreetNumber(streetNumber != null ? streetNumber.trim() : "")
                .setStreetName(streetName.trim())
                .setSuburbName(suburbName != null ? suburbName.trim() : "")
                .setCity(city.trim())
                .setPostalCode(postalCode != null ? postalCode.trim() : "")
                .setProvince(province != null ? province.trim() : "")
                .setCountry(country.trim())
                .build();
    }


    public static Address createAddress(String streetNumber,
                                        String streetName,
                                        String suburbName,
                                        String city,
                                        String postalCode,
                                        String province,
                                        String country) {

        return createAddress(Helper.generateId(), streetNumber, streetName, suburbName, city, postalCode, province, country);
    }
}