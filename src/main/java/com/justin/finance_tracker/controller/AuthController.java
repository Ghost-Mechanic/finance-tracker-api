package com.justin.finance_tracker.controller;

import com.justin.finance_tracker.dto.LoginDto;
import com.justin.finance_tracker.dto.RegisterDto;
import com.justin.finance_tracker.service.AuthService;
import com.justin.finance_tracker.util.ESystemStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        ESystemStatus status = authService.register(registerDto);

        // Return structured response
        return ResponseEntity.ok(
                Collections.singletonMap("status", status)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody @Valid LoginDto loginDto) {
        String token = authService.login(loginDto);

        if (token != null) {
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }

}
