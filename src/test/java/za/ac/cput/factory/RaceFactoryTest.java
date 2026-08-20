package za.ac.cput.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Race;

import static org.junit.jupiter.api.Assertions.*;

/*
 * RaceFactoryTest.java
 * Author: Inga Plati
 * 230126634
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class RaceFactoryTest {

    @Test
    @Order(1)
    void createRaceValid() {
        Race race = RaceFactory.createRace("Black African");
        assertNotNull(race);
        assertNotNull(race.getRaceId());
        assertEquals("Black African", race.getDescription());
        System.out.println("Valid Race: " + race);
    }

    @Test
    @Order(2)
    void createRaceWithSuppliedId() {
        Race race = RaceFactory.createRace("RACE001", "Coloured");
        assertNotNull(race);
        assertEquals("RACE001", race.getRaceId());
        assertEquals("Coloured", race.getDescription());
        System.out.println("Race with Supplied ID: " + race);
    }

    @Test
    @Order(3)
    void createRaceTrimsDescription() {
        Race race = RaceFactory.createRace("  White  ");
        assertNotNull(race);
        assertEquals("White", race.getDescription());
    }

    @Test
    @Order(4)
    void createRaceNullDescriptionReturnsNull() {
        Race race = RaceFactory.createRace(null);
        assertNull(race);
    }

    @Test
    @Order(5)
    void createRaceEmptyDescriptionReturnsNull() {
        Race race = RaceFactory.createRace("   ");
        assertNull(race);
    }

    @Test
    @Order(6)
    void createRaceOtherCategoryValid() {
        Race race = RaceFactory.createRace("Other");
        assertNotNull(race);
        assertNotNull(race.getRaceId());
        assertEquals("Other", race.getDescription());
        System.out.println("Race with Other Category: " + race);
    }
}