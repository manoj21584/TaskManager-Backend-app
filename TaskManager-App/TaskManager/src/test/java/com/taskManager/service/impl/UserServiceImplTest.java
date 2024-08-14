package com.taskManager.service.impl;

import com.taskManager.entity.User;
import com.taskManager.exception.ResourceNotFoundException;
import com.taskManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserToDb_ShouldSaveUserAndReturnSuccessMessage() {

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setTimeZone("America/New_York");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        String result = userService.createUserToDb(user);


        verify(userRepository, times(1)).save(user);
        assertEquals("User saved to db", result);
    }

    @Test
    void updateUserInDb_ShouldUpdateUserAndReturnSuccessMessage() {

        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setFirstName("Jane");
        existingUser.setLastName("Doe");

        User updatedUser = new User();
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Smith");
        updatedUser.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        String result = userService.updateUserInDb(updatedUser, userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
        assertEquals("updated successfully", result);
        assertEquals("John", existingUser.getFirstName());
        assertEquals("Smith", existingUser.getLastName());
        assertTrue(existingUser.isActive());
    }

    @Test
    void updateUserInDb_ShouldThrowExceptionWhenUserNotFound() {

        Long userId = 1L;
        User updatedUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserInDb(updatedUser, userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteUserInDb_ShouldDeleteUserAndReturnSuccessMessage() {

        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        String result = userService.deleteUserInDb(userId);


        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
        assertEquals("User deleted successfully", result);
    }

    @Test
    void deleteUserInDb_ShouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserInDb(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).deleteById(userId);
    }

    @Test
    void getUserById_ShouldReturnUserWhenFound() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setFirstName("John");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User result = userService.getUserById(userId);

        verify(userRepository, times(1)).findById(userId);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void getUserById_ShouldThrowExceptionWhenUserNotFound() {

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}
