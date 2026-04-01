package com.example.scaler_capstone_project.services;

import com.example.scaler_capstone_project.models.User;
import com.example.scaler_capstone_project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;
    private User newUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("encodedPassword");

        newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser.setPassword("encodedPassword");
    }

    @Test
    void testSignup_Success() {
        // Arrange
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userService.signup("New User", "new@example.com", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("New User", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository, times(1)).findByEmail("new@example.com");
        verify(bCryptPasswordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignup_EmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> userService.signup("Existing User", "existing@example.com", "password123"));
        assertEquals("User with this email existing@example.com is already registered", exception.getMessage());

        verify(userRepository, times(1)).findByEmail("existing@example.com");
        verify(bCryptPasswordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
