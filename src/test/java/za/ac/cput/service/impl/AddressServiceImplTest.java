package za.ac.cput.service.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Address;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddressServiceImplTest {

    @Autowired
    private AddressServiceImpl addressService;

    private static final String ADDRESS_ID = "ADDR-001";
    private static final String CITY = "Cape Town";
    private static final String PROVINCE = "Western Cape";
    private static final String POSTAL_CODE = "8000";

    private static final Address address = new Address.Builder()
            .setAddressId(ADDRESS_ID)
            .setStreetNumber("12")
            .setStreetName("Hanover Street")
            .setSuburbName("District Six")
            .setCity(CITY)
            .setPostalCode(POSTAL_CODE)
            .setProvince(PROVINCE)
            .setCountry("South Africa")
            .build();

    @Test
    @Order(1)
    void create() {
        Address created = addressService.create(address);
        assertNotNull(created);
        assertEquals(address.getAddressId(), created.getAddressId());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        Address read = addressService.read(address.getAddressId());
        assertNotNull(read);
        assertEquals(address.getCity(), read.getCity());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void update() {
        Address updatedAddress = new Address.Builder()
                .copy(address)
                .setSuburbName("Zonnebloem")
                .build();
        Address updated = addressService.update(updatedAddress);
        assertNotNull(updated);
        assertEquals("Zonnebloem", updated.getSuburbName());
        System.out.println("Updated: " + updated);
    }

    @Test
    @Order(4)
    void findAddressByCity() {
        List<Address> addresses = addressService.findAddressByCity(CITY);
        assertNotNull(addresses);
        assertFalse(addresses.isEmpty());
        System.out.println("Found by City: " + addresses);
    }

    @Test
    @Order(5)
    void findAddressByProvince() {
        List<Address> addresses = addressService.findAddressByProvince(PROVINCE);
        assertNotNull(addresses);
        assertFalse(addresses.isEmpty());
        System.out.println("Found by Province: " + addresses);
    }

    @Test
    @Order(6)
    void findAddressByPostalCode() {
        List<Address> addresses = addressService.findAddressByPostalCode(POSTAL_CODE);
        assertNotNull(addresses);
        assertFalse(addresses.isEmpty());
        System.out.println("Found by Postal Code: " + addresses);
    }

    @Test
    @Order(7)
    void getAll() {
        List<Address> all = addressService.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        System.out.println("All Addresses: " + all);
    }

    @Test
    @Order(8)
    void delete() {
        addressService.delete(address.getAddressId());
        Address read = addressService.read(address.getAddressId());
        assertNull(read);
        System.out.println("Deleted address with ID: " + address.getAddressId());
    }
}