package za.ac.cput.service.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Address;
import za.ac.cput.factory.AddressFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddressServiceImplTest {

    @Autowired
    private AddressServiceImpl addressService;

    private static Address address = AddressFactory.createAddress(
            "ADDR-001",
            "12",
            "Hanover Street",
            "District Six",
            "Cape Town",
            "8000",
            "Western Cape",
            "South Africa"
    );

    @Test
    @Order(1)
    void create() {
        Address address1 = addressService.create(address);
        assertNotNull(address1);
        System.out.println(address1);
    }

    @Test
    @Order(2)
    void read() {
        Address address1 = addressService.read(address.getAddressId());
        System.out.println(address1);
    }

    @Test
    @Order(3)
    void update() {
        Address address1 = new Address.Builder().copy(address)
                .setSuburbName("Zonnebloem")
                .build();

        Address updatedAddress = addressService.update(address1);
        System.out.println(updatedAddress);
    }

    @Test
    @Disabled
    void delete() {
        addressService.delete(address.getAddressId());
        Address read = addressService.read(address.getAddressId());
        assertNull(read);
    }

    @Test
    @Order(4)
    void getAll() {
        List<Address> addresses = addressService.getAll();
        System.out.println(addresses);
    }

    @Test
    @Order(5)
    void findAddressByCity() {
        List<Address> addresses = addressService.findAddressByCity("Cape Town");
        System.out.println(addresses);
    }

    @Test
    @Order(6)
    void findAddressByProvince() {
        List<Address> addresses = addressService.findAddressByProvince("Western Cape");
        System.out.println(addresses);
    }

    @Test
    @Order(7)
    void findAddressByPostalCode() {
        List<Address> addresses = addressService.findAddressByPostalCode("8000");
        System.out.println(addresses);
    }
}