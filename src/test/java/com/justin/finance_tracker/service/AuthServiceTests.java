package com.justin.finance_tracker.service;

import com.justin.finance_tracker.dto.LoginDto;
import com.justin.finance_tracker.dto.RegisterDto;
import com.justin.finance_tracker.model.User;
import com.justin.finance_tracker.repository.UserRepository;
import com.justin.finance_tracker.util.ESystemStatus;
import com.justin.finance_tracker.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class AuthServiceTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- SUCCESS ----------
    @Test
    void registerSuccess() {
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
    void registerPasswordMismatch() {
        RegisterDto dto = createDto("testuser", "test@example.com", "Password1!", "Password2!");

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.PASSWORD_MISMATCH, status);
        verify(userRepository, never()).save(any());
    }

    // ---------- INVALID PASSWORD ----------
    @Test
    void registerInvalidPassword() {
        RegisterDto dto = createDto("testuser", "test@example.com", "pass", "pass");

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.INVALID_PASSWORD, status);
        verify(userRepository, never()).save(any());
    }

    // ---------- USERNAME TAKEN ----------
    @Test
    void registerUsernameTaken() {
        RegisterDto dto = createDto("testuser", "test@example.com", "Password1!", "Password1!");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        ESystemStatus status = authService.register(dto);

        assertEquals(ESystemStatus.USERNAME_TAKEN, status);
        verify(userRepository, never()).save(any());
    }

    // ---------- EMAIL TAKEN ----------
    @Test
    void registerEmailTaken() {
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

    // ---------------- LOGIN TESTS ----------------

    // ---------- SUCCESS ----------
    @Test
    void loginSuccess() {
        String email = "test@example.com";
        String rawPassword = "Password1!";
        String encodedPassword = "encodedPassword";
        String token = "mock-jwt-token";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(user.getId(), email)).thenReturn(token);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(rawPassword);

        String result = authService.login(loginDto);

        assertEquals(token, result);
    }

    // ---------- INVALID PASSWORD ----------
    @Test
    void loginInvalidPassword() {
        String email = "test@example.com";
        String rawPassword = "WrongPassword";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(encodedPassword);

        String result = authService.login(loginDto);

        assertNull(result);
    }

    // ---------- NON-EXISTENT EMAIL ----------
    @Test
    void loginNonExistentEmail() {
        String email = "nonexistent@example.com";
        String password = "Password1!";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);

        String result = authService.login(loginDto);

        assertNull(result);
    }
}
