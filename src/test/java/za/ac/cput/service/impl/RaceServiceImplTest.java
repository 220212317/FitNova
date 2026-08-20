/*
 * RaceServiceImplTest.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Race;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RaceServiceImplTest {

    @Autowired
    private RaceServiceImpl service;

    private static Race race;

    @Test
    @Order(1)
    void createRace() {
        race = new Race.Builder().setRaceId("RACE-TEST-001").setDescription("Black African").build();
        Race created = service.create(race);
        assertNotNull(created);
        assertEquals("Black African", created.getDescription());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void readRace() {
        Race read = service.read(race.getRaceId());
        assertNotNull(read);
        assertEquals(race.getRaceId(), read.getRaceId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void updateRace() {
        Race updated = new Race.Builder().copy(race).setDescription("Updated Description").build();
        Race result = service.update(updated);
        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void getAllRaces() {
        List<Race> races = service.getAll();
        assertNotNull(races);
        assertFalse(races.isEmpty());
        System.out.println("All Races: " + races);
    }

    @Test
    @Order(5)
    void deleteRace() {
        service.delete(race.getRaceId());
        Race deleted = service.read(race.getRaceId());
        assertNull(deleted);
    }
}