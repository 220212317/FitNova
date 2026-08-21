/*
 * RaceControllerTest.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import za.ac.cput.domain.Race;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RaceControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static Race race;

    @Test
    @Order(1)
    void createRace() {
        Race toCreate = new Race.Builder().setRaceId("RACE-CTRL-001").setDescription("Black African").build();
        race = restTemplate.postForObject("/race/create", toCreate, Race.class);
        assertNotNull(race);
        assertEquals("Black African", race.getDescription());
        System.out.println("Created: " + race);
    }

    @Test
    @Order(2)
    void readRace() {
        Race read = restTemplate.getForObject("/race/read/" + race.getRaceId(), Race.class);
        assertNotNull(read);
        assertEquals(race.getRaceId(), read.getRaceId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void updateRace() {
        Race updated = new Race.Builder().copy(race).setDescription("Updated Description").build();
        restTemplate.put("/race/update", updated);
        Race result = restTemplate.getForObject("/race/read/" + race.getRaceId(), Race.class);
        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void getAllRaces() {
        Race[] races = restTemplate.getForObject("/race/getAll", Race[].class);
        assertNotNull(races);
        assertTrue(races.length > 0);
        System.out.println("All Races count: " + races.length);
    }

    @Test
    @Order(5)
    void deleteRace() {
        restTemplate.delete("/race/delete/" + race.getRaceId());
        Race deleted = restTemplate.getForObject("/race/read/" + race.getRaceId(), Race.class);
        assertNull(deleted);
    }
}