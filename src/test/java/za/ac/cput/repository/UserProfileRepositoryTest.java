package za.ac.cput.repository;


/**
 * 26March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.entity.UserProfile;
import za.ac.cput.factory.UserProfileFactory;
import za.ac.cput.repository.impl.UserProfileRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileRepositoryTest {

    private UserProfileRepositoryImpl repo;

    private UserProfile profile1;
    private UserProfile profile2;

    @BeforeEach
    void setUp() {
        repo = UserProfileRepositoryImpl.getRepository();

        // Clear state between tests by deleting known IDs
        repo.delete("P001");
        repo.delete("P002");

        profile1 = UserProfileFactory.CreateUserProfile(
                "P001", "Avuyile", "Sitoyi", "Male",
                LocalDate.of(2000, 5, 15));

        profile2 = UserProfileFactory.CreateUserProfile(
                "P002", "Sipho", "Ndlovu", "Male",
                LocalDate.of(1999, 3, 10));
    }


    @Test
    void testGetRepository_ReturnsSameInstance() {
        UserProfileRepositoryImpl instance1 = UserProfileRepositoryImpl.getRepository();
        UserProfileRepositoryImpl instance2 = UserProfileRepositoryImpl.getRepository();
        assertSame(instance1, instance2);
    }


    @Test
    void testCreate_NotNull() {
        UserProfile created = repo.create(profile1);
        assertNotNull(created);
    }

    @Test
    void testCreate_ReturnsCorrectProfile() {
        UserProfile created = repo.create(profile1);
        assertEquals(profile1.getProfileId(), created.getProfileId());
    }

    @Test
    void testCreate_StoresProfile() {
        repo.create(profile1);
        UserProfile found = repo.read("P001");
        assertNotNull(found);
        assertEquals("P001", found.getProfileId());
    }

    @Test
    void testCreate_MultipleProfiles() {
        repo.create(profile1);
        repo.create(profile2);
        assertNotNull(repo.read("P001"));
        assertNotNull(repo.read("P002"));
    }


    @Test
    void testRead_ExistingProfile_NotNull() {
        repo.create(profile1);
        assertNotNull(repo.read("P001"));
    }

    @Test
    void testRead_ExistingProfile_CorrectFirstName() {
        repo.create(profile1);
        assertEquals("Avuyile", repo.read("P001").getFirstName());
    }

    @Test
    void testRead_ExistingProfile_CorrectLastName() {
        repo.create(profile1);
        assertEquals("Sitoyi", repo.read("P001").getLastName());
    }

    @Test
    void testRead_NonExistentProfile_ReturnsNull() {
        assertNull(repo.read("DOES_NOT_EXIST"));
    }


    @Test
    void testReadAll_NotNull() {
        assertNotNull(repo.getAll());
    }

    @Test
    void testReadAll_ContainsBothProfiles() {
        repo.create(profile1);
        repo.create(profile2);
        List<UserProfile> all = repo.getAll();
        assertTrue(all.stream().anyMatch(p -> p.getProfileId().equals("P001")));
        assertTrue(all.stream().anyMatch(p -> p.getProfileId().equals("P002")));
    }

    @Test
    void testReadAll_SizeIncreasesAfterCreate() {
        int sizeBefore = repo.getAll().size();
        repo.create(profile1);
        assertEquals(sizeBefore + 1, repo.getAll().size());
    }


    @Test
    void testUpdate_ExistingProfile_UpdatesFirstName() {
        repo.create(profile1);

        UserProfile updated = UserProfileFactory.CreateUserProfile(
                "P001", "Liam", "Sitoyi", "Male",
                LocalDate.of(2000, 5, 15));

        repo.update(updated);
        assertEquals("Liam", repo.read("P001").getFirstName());
    }

    @Test
    void testUpdate_ExistingProfile_UpdatesLastName() {
        repo.create(profile1);

        UserProfile updated = UserProfileFactory.CreateUserProfile(
                "P001", "Avuyile", "Dlamini", "Male",
                LocalDate.of(2000, 5, 15));

        repo.update(updated);
        assertEquals("Dlamini", repo.read("P001").getLastName());
    }

    @Test
    void testUpdate_NonExistentProfile_DoesNotStore() {
        // updating a profile that was never created should not add it
        UserProfile ghost = UserProfileFactory.CreateUserProfile(
                "P999", "Ghost", "User", "Female",
                LocalDate.of(1995, 1, 1));

        repo.update(ghost);
        assertNull(repo.read("P999"));
    }

    @Test
    void testUpdate_ReturnsUpdatedProfile() {
        repo.create(profile1);

        UserProfile updated = UserProfileFactory.CreateUserProfile(
                "P001", "Avuyile", "Updated", "Male",
                LocalDate.of(2000, 5, 15));

        UserProfile result = repo.update(updated);
        assertEquals("Updated", result.getLastName());
    }


    @Test
    void testDelete_ExistingProfile_IsRemoved() {
        repo.create(profile1);
        repo.delete("P001");
        assertNull(repo.read("P001"));
    }

    @Test
    void testDelete_NonExistentId_NoException() {
        assertDoesNotThrow(() -> repo.delete("DOES_NOT_EXIST"));
    }

    @Test
    void testDelete_NullId_NoException() {
        assertDoesNotThrow(() -> repo.delete(null));
    }

    @Test
    void testDelete_OnlyRemovesTargetProfile() {
        repo.create(profile1);
        repo.create(profile2);
        repo.delete("P001");
        assertNull(repo.read("P001"));
        assertNotNull(repo.read("P002"));
    }

    @Test
    void testDelete_SizeDecreasesAfterDelete() {
        repo.create(profile1);
        repo.create(profile2);
        int sizeBefore = repo.getAll().size();
        repo.delete("P001");
        assertEquals(sizeBefore - 1, repo.getAll().size());
    }
}
