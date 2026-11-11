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
        gender = genderService.create(new Gender.Builder().setGenderId("GEN-DEMO-TEST").setDescription("Demographic Test Gender").build());
        race = raceService.create(new Race.Builder().setRaceId("RACE-DEMO-TEST").setDescription("Demographic Test Race").build());

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

    @Test
    @Order(6)
    void rejectsDuplicateGenderAndRacePair() {
        Gender g = genderService.create(new Gender.Builder()
                .setGenderId("GEN-DUP-PAIR").setDescription("Pair Test Gender").build());
        Race r = raceService.create(new Race.Builder()
                .setRaceId("RACE-DUP-PAIR").setDescription("Pair Test Race").build());
        Demographic first = service.create(new Demographic.Builder()
                .setDemographyId("DEM-DUP-001").setGender(g).setRace(r).build());
        try {
            assertThrows(IllegalStateException.class, () -> service.create(new Demographic.Builder()
                    .setDemographyId("DEM-DUP-002").setGender(g).setRace(r).build()));
        } finally {
            service.delete(first.getDemographyId());
            genderService.delete(g.getGenderId());
            raceService.delete(r.getRaceId());
        }
    }

    @Test
    @Order(7)
    void rejectsDemographicWithNoGenderOrRace() {
        assertThrows(IllegalArgumentException.class, () -> service.create(new Demographic.Builder()
                .setDemographyId("DEM-EMPTY-001").build()));
    }
}
