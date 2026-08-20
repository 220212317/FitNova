/*
 * UserServiceImplTest.java
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
import za.ac.cput.repository.IUserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User buildUser() {
        return new User.Builder()
                .setUserId("U001")
                .setFirstName("Thando")
                .setLastName("Nkosi")
                .setDateOfBirth(LocalDate.of(1999, 5, 20))
                .build();
    }

    @Test
    void create() {
        User user = buildUser();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertNotNull(result);
        assertEquals("U001", result.getUserId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void readFound() {
        User user = buildUser();
        when(userRepository.findById("U001")).thenReturn(Optional.of(user));

        User result = userService.read("U001");

        assertNotNull(result);
        assertEquals("Thando", result.getFirstName());
    }

    @Test
    void readNotFound() {
        when(userRepository.findById("U999")).thenReturn(Optional.empty());

        User result = userService.read("U999");

        assertNull(result);
    }

    @Test
    void updateWhenExists() {
        User user = buildUser();
        when(userRepository.existsById("U001")).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.update(user);

        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateWhenNotExists() {
        User user = buildUser();
        when(userRepository.existsById("U001")).thenReturn(false);

        User result = userService.update(user);

        assertNull(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteWhenExists() {
        when(userRepository.existsById("U001")).thenReturn(true);

        boolean result = userService.delete("U001");

        assertTrue(result);
        verify(userRepository, times(1)).deleteById("U001");
    }

    @Test
    void deleteWhenNotExists() {
        when(userRepository.existsById("U001")).thenReturn(false);

        boolean result = userService.delete("U001");

        assertFalse(result);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getAll() {
        User user = buildUser();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByFirstNameAndLastName() {
        User user = buildUser();
        when(userRepository.findByFirstNameAndLastName("Thando", "Nkosi")).thenReturn(List.of(user));

        List<User> result = userService.findByFirstNameAndLastName("Thando", "Nkosi");

        assertEquals(1, result.size());
    }

    @Test
    void searchByLastName() {
        User user = buildUser();
        when(userRepository.findByLastNameContainingIgnoreCase("nko")).thenReturn(List.of(user));

        List<User> result = userService.searchByLastName("nko");

        assertEquals(1, result.size());
    }
}