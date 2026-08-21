package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Address;
import za.ac.cput.factory.AddressFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddressControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "/address";

    private static Address address = AddressFactory.createAddress(
            "ADDR-CTRL-001",
            "12",
            "Long Street",
            "Gardens",
            "Cape Town",
            "8000",
            "Western Cape",
            "South Africa"
    );

    @Test
    @Order(1)
    void create() {
        String url = baseUrl + "/create";
        ResponseEntity<Address> response = restTemplate.postForEntity(url, address, Address.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Address createdAddress = response.getBody();
        System.out.println("Created: " + createdAddress);
    }

    @Test
    @Order(2)
    void read() {
        String url = baseUrl + "/read/" + address.getAddressId();
        ResponseEntity<Address> response = restTemplate.getForEntity(url, Address.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Address readAddress = response.getBody();
        System.out.println("Read: " + readAddress);
    }

    @Test
    @Order(3)
    void readNotFound() {
        String url = baseUrl + "/read/NON-EXISTENT-ID";
        ResponseEntity<Address> response = restTemplate.getForEntity(url, Address.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void update() {
        String url = baseUrl + "/update";
        Address updatedAddress = new Address.Builder().copy(address)
                .setStreetNumber("99")
                .build();

        HttpEntity<Address> entity = new HttpEntity<>(updatedAddress);
        ResponseEntity<Address> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Address.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("99", response.getBody().getStreetNumber());
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Disabled
    void delete() {
        String url = baseUrl + "/delete/" + address.getAddressId();
        restTemplate.delete(url);
        ResponseEntity<Address> response = restTemplate.getForEntity(baseUrl + "/read/" + address.getAddressId(), Address.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(5)
    void findAddressByCity() {
        String url = baseUrl + "/findByCity/" + address.getCity();
        ResponseEntity<Address[]> response = restTemplate.getForEntity(url, Address[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by City: " + List.of(response.getBody()));
    }

    @Test
    @Order(6)
    void findAddressByProvince() {
        String url = baseUrl + "/findByProvince/" + address.getProvince();
        ResponseEntity<Address[]> response = restTemplate.getForEntity(url, Address[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by Province: " + List.of(response.getBody()));
    }

    @Test
    @Order(7)
    void findAddressByPostalCode() {
        String url = baseUrl + "/findByPostalCode/" + address.getPostalCode();
        ResponseEntity<Address[]> response = restTemplate.getForEntity(url, Address[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by Postal Code: " + List.of(response.getBody()));
    }

    @Test
    @Order(8)
    void getAll() {
        String url = baseUrl + "/getAll";
        ResponseEntity<Address[]> response = restTemplate.getForEntity(url, Address[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("All Addresses: " + List.of(response.getBody()));
    }
}