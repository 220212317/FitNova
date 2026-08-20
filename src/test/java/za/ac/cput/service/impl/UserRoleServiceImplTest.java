/*
 * UserRoleServiceImplTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IUserRoleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private IUserRoleRepository userRoleRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    private User buildUser() {
        return new User.Builder()
                .setUserId("U001")
                .setFirstName("Thando")
                .setLastName("Nkosi")
                .setDateOfBirth(LocalDate.of(1999, 5, 20))
                .build();
    }

    private UserRole buildUserRole() {
        return new UserRole.Builder()
                .setUserRoleId("UR001")
                .setUser(buildUser())
                .setRoleId(RoleType.MEMBER)
                .setDescription("Standard gym member")
                .build();
    }

    @Test
    void create() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.save(userRole)).thenReturn(userRole);

        UserRole result = userRoleService.create(userRole);

        assertNotNull(result);
        assertEquals(RoleType.MEMBER, result.getRoleId());
    }

    @Test
    void readFound() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.findById("UR001")).thenReturn(Optional.of(userRole));

        UserRole result = userRoleService.read("UR001");

        assertNotNull(result);
    }

    @Test
    void readNotFound() {
        when(userRoleRepository.findById("UR999")).thenReturn(Optional.empty());

        UserRole result = userRoleService.read("UR999");

        assertNull(result);
    }

    @Test
    void updateWhenExists() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.existsById("UR001")).thenReturn(true);
        when(userRoleRepository.save(userRole)).thenReturn(userRole);

        UserRole result = userRoleService.update(userRole);

        assertNotNull(result);
    }

    @Test
    void updateWhenNotExists() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.existsById("UR001")).thenReturn(false);

        UserRole result = userRoleService.update(userRole);

        assertNull(result);
        verify(userRoleRepository, never()).save(any());
    }

    @Test
    void deleteWhenExists() {
        when(userRoleRepository.existsById("UR001")).thenReturn(true);

        boolean result = userRoleService.delete("UR001");

        assertTrue(result);
        verify(userRoleRepository, times(1)).deleteById("UR001");
    }

    @Test
    void deleteWhenNotExists() {
        when(userRoleRepository.existsById("UR001")).thenReturn(false);

        boolean result = userRoleService.delete("UR001");

        assertFalse(result);
        verify(userRoleRepository, never()).deleteById(any());
    }

    @Test
    void getAll() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.findAll()).thenReturn(List.of(userRole));

        List<UserRole> result = userRoleService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByUser() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.findByUser_UserId("U001")).thenReturn(List.of(userRole));

        List<UserRole> result = userRoleService.findByUser("U001");

        assertEquals(1, result.size());
    }

    @Test
    void findByRole() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.findByRoleId(RoleType.MEMBER)).thenReturn(List.of(userRole));

        List<UserRole> result = userRoleService.findByRole(RoleType.MEMBER);

        assertEquals(1, result.size());
    }

    @Test
    void findByUserAndRole() {
        UserRole userRole = buildUserRole();
        when(userRoleRepository.findByUser_UserIdAndRoleId("U001", RoleType.MEMBER))
                .thenReturn(Optional.of(userRole));

        Optional<UserRole> result = userRoleService.findByUserAndRole("U001", RoleType.MEMBER);

        assertTrue(result.isPresent());
    }
}