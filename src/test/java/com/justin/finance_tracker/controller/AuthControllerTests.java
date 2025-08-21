package com.justin.finance_tracker.controller;

import com.justin.finance_tracker.dto.LoginDto;
import com.justin.finance_tracker.dto.RegisterDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justin.finance_tracker.service.AuthService;
import com.justin.finance_tracker.util.ESystemStatus;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService; // mock the login behavior

    @Test
    void registerUserSuccess() throws Exception {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPassword("Password1!");
        dto.setConfirmPassword("Password1!");

        // Mock the service to return a fake token
        Mockito.when(authService.register(dto))
                .thenReturn(ESystemStatus.valueOf("SUCCESS"));

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void registerUserPasswordMismatch() throws Exception {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPassword("Password1!");
        dto.setConfirmPassword("Password2!");

        // Mock the service to return a fake token
        Mockito.when(authService.register(dto))
                .thenReturn(ESystemStatus.valueOf("PASSWORD_MISMATCH"));

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PASSWORD_MISMATCH"));
    }

    @Test
    void loginSuccess() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("justin@test.com");
        dto.setPassword("Mypassword1!23");

        // Mock the service to return a fake token
        Mockito.when(authService.login(dto))
                .thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void loginInvalidPassword() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("justin@test.com");
        dto.setPassword("wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void loginNonExistentEmail() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("doesnotexist@test.com");
        dto.setPassword("SomePassword1!");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void loginBlankEmail() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("");
        dto.setPassword("Mypassword1!23");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.email").value("Email is required"));
    }

    @Test
    void loginInvalidEmailFormat() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("invalid-email");
        dto.setPassword("Mypassword1!23");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.email").value("Email is invalid"));
    }

    @Test
    void loginBlankPassword() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("justin@test.com");
        dto.setPassword("");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.password").value("Password is required"));
    }
}
