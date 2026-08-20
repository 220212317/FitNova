package za.ac.cput.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Address;

import static org.junit.jupiter.api.Assertions.*;

/*
 * AddressFactoryTest.java
 * Author: Athi Sintiya
 * 220212317
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class AddressFactoryTest {

    @Test
    @Order(1)
    void createAddressValid() {
        Address address = AddressFactory.createAddress(
                "10",
                "Keizersgracht St",
                "District Six",
                "Cape Town",
                "8001",
                "Western Cape",
                "South Africa"
        );
        assertNotNull(address);
        assertNotNull(address.getAddressId());
        assertEquals("10", address.getStreetNumber());
        assertEquals("Keizersgracht St", address.getStreetName());
        assertEquals("District Six", address.getSuburbName());
        assertEquals("Cape Town", address.getCity());
        assertEquals("8001", address.getPostalCode());
        assertEquals("Western Cape", address.getProvince());
        assertEquals("South Africa", address.getCountry());
        System.out.println("Valid Address: " + address);
    }

    @Test
    @Order(2)
    void createAddressWithSuppliedId() {
        Address address = AddressFactory.createAddress(
                "ADDR001",
                "25",
                "Adderley St",
                "Foreshore",
                "Cape Town",
                "8000",
                "Western Cape",
                "South Africa"
        );
        assertNotNull(address);
        assertEquals("ADDR001", address.getAddressId());
        assertEquals("Adderley St", address.getStreetName());
        System.out.println("Address with Supplied ID: " + address);
    }

    @Test
    @Order(3)
    void createAddressMissingStreetNameReturnsNull() {
        Address address = AddressFactory.createAddress(
                "10",
                "",
                "District Six",
                "Cape Town",
                "8001",
                "Western Cape",
                "South Africa"
        );
        assertNull(address);
    }

    @Test
    @Order(4)
    void createAddressNullStreetNameReturnsNull() {
        Address address = AddressFactory.createAddress(
                "10",
                null,
                "District Six",
                "Cape Town",
                "8001",
                "Western Cape",
                "South Africa"
        );
        assertNull(address);
    }

    @Test
    @Order(5)
    void createAddressMissingCityReturnsNull() {
        Address address = AddressFactory.createAddress(
                "10",
                "Keizersgracht St",
                "District Six",
                "",
                "8001",
                "Western Cape",
                "South Africa"
        );
        assertNull(address);
    }

    @Test
    @Order(6)
    void createAddressNullCityReturnsNull() {
        Address address = AddressFactory.createAddress(
                "10",
                "Keizersgracht St",
                "District Six",
                null,
                "8001",
                "Western Cape",
                "South Africa"
        );
        assertNull(address);
    }

    @Test
    @Order(7)
    void createAddressMissingCountryReturnsNull() {
        Address address = AddressFactory.createAddress(
                "10",
                "Keizersgracht St",
                "District Six",
                "Cape Town",
                "8001",
                "Western Cape",
                ""
        );
        assertNull(address);
    }

    @Test
    @Order(8)
    void createAddressNullCountryReturnsNull() {
        Address address = AddressFactory.createAddress(
                "10",
                "Keizersgracht St",
                "District Six",
                "Cape Town",
                "8001",
                "Western Cape",
                null
        );
        assertNull(address);
    }
}