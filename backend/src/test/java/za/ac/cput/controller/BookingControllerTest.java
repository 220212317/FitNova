package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.BookingStatus;
import za.ac.cput.domain.enums.SlotStatus;
import za.ac.cput.factory.BookingFactory;
import za.ac.cput.repository.IAvailabilitySlotRepository;
import za.ac.cput.repository.IUserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAvailabilitySlotRepository availabilitySlotRepository;

    private String baseUrl = "/booking";

    private Booking booking;

    // Foreign-key dependencies (member, trainer, slot) must exist in the
    // database before a Booking referencing them can be created, so they're
    // persisted once here rather than in a plain static field.
    @BeforeAll
    void setUp() {
        User member = new User.Builder()
                .setUserId("U-CTRL-MEMBER-001")
                .setFirstName("Bandile")
                .setLastName("Mbethe")
                .setDateOfBirth(LocalDate.of(2000, 3, 14))
                .build();
        member = userRepository.saveAndFlush(member);

        User trainer = new User.Builder()
                .setUserId("U-CTRL-TRAINER-001")
                .setFirstName("Fanta")
                .setLastName("Machethe")
                .setDateOfBirth(LocalDate.of(1990, 6, 1))
                .build();
        trainer = userRepository.saveAndFlush(trainer);
        assertTrue(userRepository.existsById(trainer.getUserId()),
                "trainer row should exist in the database immediately after saveAndFlush");

        AvailabilitySlot slot = new AvailabilitySlot.Builder()
                .setSlotId("S-CTRL-001")
                .setDate(LocalDate.now().plusDays(1))
                .setStartTime(LocalTime.of(8, 0))
                .setEndTime(LocalTime.of(9, 0))
                .setStatus(SlotStatus.AVAILABLE)
                .setTrainer(trainer)
                .build();

        availabilitySlotRepository.saveAndFlush(slot);

        booking = BookingFactory.createBooking(
                "B-CTRL-001",
                LocalDateTime.now().plusDays(1),
                BookingStatus.CONFIRMED,
                member,
                slot
        );
    }

    @Test
    @Order(1)
    void create() {
        String url = baseUrl + "/create";
        ResponseEntity<Booking> response = restTemplate.postForEntity(url, booking, Booking.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Booking createdBooking = response.getBody();
        System.out.println("Created: " + createdBooking);
    }

    @Test
    @Order(2)
    void read() {
        String url = baseUrl + "/read/" + booking.getBookingId();
        ResponseEntity<Booking> response = restTemplate.getForEntity(url, Booking.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Booking readBooking = response.getBody();
        System.out.println("Read: " + readBooking);
    }

    @Test
    @Order(3)
    void readNotFound() {
        String url = baseUrl + "/read/NON-EXISTENT-ID";
        ResponseEntity<Booking> response = restTemplate.getForEntity(url, Booking.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void update() {
        String url = baseUrl + "/update";
        Booking updatedBooking = new Booking.Builder().copy(booking)
                .setStatus(BookingStatus.COMPLETED)
                .build();

        HttpEntity<Booking> entity = new HttpEntity<>(updatedBooking);
        ResponseEntity<Booking> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Booking.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BookingStatus.COMPLETED, response.getBody().getStatus());
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Disabled
    void delete() {
        String url = baseUrl + "/delete/" + booking.getBookingId();
        restTemplate.delete(url);
        ResponseEntity<Booking> response = restTemplate.getForEntity(baseUrl + "/read/" + booking.getBookingId(), Booking.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(5)
    void findByMember() {
        String url = baseUrl + "/findByMember/" + booking.getMember().getUserId();
        ResponseEntity<Booking[]> response = restTemplate.getForEntity(url, Booking[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by Member: " + List.of(response.getBody()));
    }

    @Test
    @Order(6)
    void findBySlot() {
        String url = baseUrl + "/findBySlot/" + booking.getSlot().getSlotId();
        ResponseEntity<Booking[]> response = restTemplate.getForEntity(url, Booking[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by Slot: " + List.of(response.getBody()));
    }

    @Test
    @Order(7)
    void findByStatus() {
        String url = baseUrl + "/findByStatus/" + BookingStatus.COMPLETED;
        ResponseEntity<Booking[]> response = restTemplate.getForEntity(url, Booking[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by Status: " + List.of(response.getBody()));
    }

    @Test
    @Order(8)
    void getAll() {
        String url = baseUrl + "/getAll";
        ResponseEntity<Booking[]> response = restTemplate.getForEntity(url, Booking[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("All Bookings: " + List.of(response.getBody()));
    }

}