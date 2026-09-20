/*
 * DemographicServiceImplTest.java
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
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Race;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemographicServiceImplTest {

    @Autowired
    private DemographicServiceImpl service;

    @Autowired
    private GenderServiceImpl genderService;

    @Autowired
    private RaceServiceImpl raceService;

    private static Demographic demographic;
    private static Gender gender;
    private static Race race;

    @Test
    @Order(1)
    void createDemographic() {
        gender = genderService.create(new Gender.Builder().setGenderId("GEN-DEMO-TEST").setDescription("Male").build());
        race = raceService.create(new Race.Builder().setRaceId("RACE-DEMO-TEST").setDescription("Black African").build());

        demographic = new Demographic.Builder()
                .setDemographyId("DEM-TEST-001")
                .setGender(gender)
                .setRace(race)
                .build();

        Demographic created = service.create(demographic);
        assertNotNull(created);
        assertEquals(gender.getGenderId(), created.getGender().getGenderId());
        assertEquals(race.getRaceId(), created.getRace().getRaceId());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void readDemographic() {
        Demographic read = service.read(demographic.getDemographyId());
        assertNotNull(read);
        assertEquals(demographic.getDemographyId(), read.getDemographyId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void updateDemographic() {
        Demographic updated = new Demographic.Builder().copy(demographic).build();
        Demographic result = service.update(updated);
        assertNotNull(result);
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void getAllDemographics() {
        List<Demographic> demographics = service.getAll();
        assertNotNull(demographics);
        assertFalse(demographics.isEmpty());
        System.out.println("All Demographics: " + demographics);
    }

    @Test
    @Order(5)
    void deleteDemographic() {
        service.delete(demographic.getDemographyId());
        Demographic deleted = service.read(demographic.getDemographyId());
        assertNull(deleted);

        genderService.delete(gender.getGenderId());
        raceService.delete(race.getRaceId());
    }
}