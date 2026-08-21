/*
 * DemographicControllerTest.java
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
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Race;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemographicControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static Demographic demographic;
    private static Gender gender;
    private static Race race;

    @Test
    @Order(1)
    void createDemographic() {
        Gender genderToCreate = new Gender.Builder().setGenderId("GEN-CTRL-DEMO").setDescription("Male").build();
        gender = restTemplate.postForObject("/gender/create", genderToCreate, Gender.class);

        Race raceToCreate = new Race.Builder().setRaceId("RACE-CTRL-DEMO").setDescription("Black African").build();
        race = restTemplate.postForObject("/race/create", raceToCreate, Race.class);

        Demographic toCreate = new Demographic.Builder()
                .setDemographyId("DEM-CTRL-001")
                .setGender(gender)
                .setRace(race)
                .build();

        demographic = restTemplate.postForObject("/demographic/create", toCreate, Demographic.class);

        assertNotNull(demographic);
        assertNotNull(demographic.getGender());
        assertNotNull(demographic.getRace());
        assertEquals(gender.getGenderId(), demographic.getGender().getGenderId());
        assertEquals(race.getRaceId(), demographic.getRace().getRaceId());
        System.out.println("Created: " + demographic);
    }

    @Test
    @Order(2)
    void readDemographic() {
        Demographic read = restTemplate.getForObject("/demographic/read/" + demographic.getDemographyId(), Demographic.class);
        assertNotNull(read);
        assertEquals(demographic.getDemographyId(), read.getDemographyId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void updateDemographic() {
        Demographic updated = new Demographic.Builder().copy(demographic).build();
        restTemplate.put("/demographic/update", updated);
        Demographic result = restTemplate.getForObject("/demographic/read/" + demographic.getDemographyId(), Demographic.class);
        assertNotNull(result);
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void getAllDemographics() {
        Demographic[] demographics = restTemplate.getForObject("/demographic/getAll", Demographic[].class);
        assertNotNull(demographics);
        assertTrue(demographics.length > 0);
        System.out.println("All Demographics count: " + demographics.length);
    }

    @Test
    @Order(5)
    void deleteDemographic() {
        restTemplate.delete("/demographic/delete/" + demographic.getDemographyId());
        Demographic deleted = restTemplate.getForObject("/demographic/read/" + demographic.getDemographyId(), Demographic.class);
        assertNull(deleted);

        restTemplate.delete("/gender/delete/" + gender.getGenderId());
        restTemplate.delete("/race/delete/" + race.getRaceId());
    }
}