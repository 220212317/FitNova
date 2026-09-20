package za.ac.cput.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Race;

import static org.junit.jupiter.api.Assertions.*;

/*
 * DemographicFactoryTest.java
 * Author: Inga Plati
 * 230126634
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class DemographicFactoryTest {

    @Test
    @Order(1)
    void createDemographicValid() {
        Gender gender = GenderFactory.createGender("Male");
        Race race = RaceFactory.createRace("Black African");

        Demographic demographic = DemographicFactory.createDemographic(gender, race);

        assertNotNull(demographic);
        assertNotNull(demographic.getDemographyId());
        assertEquals(gender, demographic.getGender());
        assertEquals(race, demographic.getRace());
        System.out.println("Valid Demographic: " + demographic);
    }

    @Test
    @Order(2)
    void createDemographicWithSuppliedId() {
        Gender gender = GenderFactory.createGender("Female");
        Race race = RaceFactory.createRace("Coloured");

        Demographic demographic = DemographicFactory.createDemographic("DEM001", gender, race);

        assertNotNull(demographic);
        assertEquals("DEM001", demographic.getDemographyId());
        assertEquals(gender, demographic.getGender());
        assertEquals(race, demographic.getRace());
        System.out.println("Demographic with Supplied ID: " + demographic);
    }

    @Test
    @Order(3)
    void createDemographicNullGenderReturnsNull() {
        Race race = RaceFactory.createRace("White");

        Demographic demographic = DemographicFactory.createDemographic(null, race);

        assertNull(demographic);
    }

    @Test
    @Order(4)
    void createDemographicNullRaceReturnsNull() {
        Gender gender = GenderFactory.createGender("Male");

        Demographic demographic = DemographicFactory.createDemographic(gender, null);

        assertNull(demographic);
    }

    @Test
    @Order(5)
    void createDemographicBothNullReturnsNull() {
        Demographic demographic = DemographicFactory.createDemographic(null, null);

        assertNull(demographic);
    }
}