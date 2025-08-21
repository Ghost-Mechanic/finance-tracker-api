package com.justin.finance_tracker.service;

import com.justin.finance_tracker.dto.RegisterDto;
import com.justin.finance_tracker.model.User;
import com.justin.finance_tracker.repository.UserRepository;
import com.justin.finance_tracker.util.ESystemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthServiceTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- SUCCESS ----------
    @Test
    void register_success() {
        RegisterDto dto = createDto("testuser", "test@example.com", "Password1!", "Password1!");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword");

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.SUCCESS, status);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ---------- PASSWORD MISMATCH ----------
    @Test
    void register_passwordMismatch() {
        RegisterDto dto = createDto("testuser", "test@example.com", "Password1!", "Password2!");

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.PASSWORD_MISMATCH, status);
        verify(userRepository, never()).save(any());
    }

    // ---------- INVALID PASSWORD ----------
    @Test
    void register_invalidPassword() {
        RegisterDto dto = createDto("testuser", "test@example.com", "pass", "pass");

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.INVALID_PASSWORD, status);
        verify(userRepository, never()).save(any());
    }

    // ---------- USERNAME TAKEN ----------
    @Test
    void register_usernameTaken() {
        RegisterDto dto = createDto("testuser", "test@example.com", "Password1!", "Password1!");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.USERNAME_TAKEN, status);
        verify(userRepository, never()).save(any());
    }

    // ---------- EMAIL TAKEN ----------
    @Test
    void register_emailTaken() {
        RegisterDto dto = createDto("testuser", "test@example.com", "Password1!", "Password1!");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.EMAIL_TAKEN, status);
        verify(userRepository, never()).save(any());
    }

    // ---------- Helper ----------
    private RegisterDto createDto(String username, String email, String password, String confirmPassword) {
        RegisterDto dto = new RegisterDto();
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setConfirmPassword(confirmPassword);
        return dto;
    }
}
