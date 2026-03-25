package com.project.fitness.services;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UpdateUserRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.model.UserRole;
import com.project.fitness.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServices userServices;

    private User mockUser;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.USER)
                .build();

        registerRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("rawPassword")
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.USER)
                .build();
    }

    // now i tested the register part

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse response = userServices.register(registerRequest);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_ThrowsException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userServices.register(registerRequest));

        assertTrue(ex.getMessage().contains("Email is already in use"));
        verify(userRepository, never()).save(any());
    }

    // authentication part test

    @Test
    void authenticate_Success() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "rawPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        User result = userServices.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals("user-123", result.getId());
    }

    @Test
    void authenticate_ThrowsException_WhenUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("unknown@example.com", "anyPass");
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userServices.authenticate(loginRequest));
    }

    @Test
    void authenticate_ThrowsException_WhenPasswordWrong() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userServices.authenticate(loginRequest));

        assertTrue(ex.getMessage().contains("invalid credentials"));
    }

    // ─── deleteUserById() ────────────────────────────────────────────────────

    @Test
    void deleteUser_ThrowsException_WhenUserNotFound() {
        when(userRepository.existsById("bad-id")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userServices.deleteUserById("bad-id"));
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById("user-123")).thenReturn(true);

        assertDoesNotThrow(() -> userServices.deleteUserById("user-123"));
        verify(userRepository, times(1)).deleteById("user-123");
    }

    // ─── updateUserById() ────────────────────────────────────────────────────

    @Test
    void updateUser_Success() {
        UpdateUserRequest updateRequest = UpdateUserRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        when(userRepository.findById("user-123")).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse response = userServices.updateUserById("user-123", updateRequest);

        assertNotNull(response);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ThrowsException_WhenUserNotFound() {
        when(userRepository.findById("bad-id")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userServices.updateUserById("bad-id", new UpdateUserRequest()));
    }

    // ─── getAllUsers() ────────────────────────────────────────────────────────

    @Test
    void getAllUsers_ReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<UserResponse> users = userServices.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("test@example.com", users.get(0).getEmail());
    }

    // ─── getUserById() ───────────────────────────────────────────────────────

    @Test
    void getUserById_Success() {
        when(userRepository.findById("user-123")).thenReturn(Optional.of(mockUser));

        UserResponse response = userServices.getUserById("user-123");

        assertNotNull(response);
        assertEquals("user-123", response.getId());
    }

    @Test
    void getUserById_ThrowsException_WhenNotFound() {
        when(userRepository.findById("bad-id")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServices.getUserById("bad-id"));
    }
}
