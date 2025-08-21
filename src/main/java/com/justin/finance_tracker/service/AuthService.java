package com.justin.finance_tracker.service;

import com.justin.finance_tracker.dto.RegisterDto;
import com.justin.finance_tracker.model.User;
import com.justin.finance_tracker.repository.UserRepository;
import com.justin.finance_tracker.util.ESystemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    private boolean isValidPassword(String password) {
        return pattern.matcher(password).matches();
    }

    public ESystemStatus register(RegisterDto registerDto) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return ESystemStatus.PASSWORD_MISMATCH;
        }
        else if (!isValidPassword(registerDto.getPassword())) {
            return ESystemStatus.INVALID_PASSWORD;
        }
        else if (userRepository.existsByUsername(registerDto.getUsername())) {
            return ESystemStatus.USERNAME_TAKEN;
        }
        else if (userRepository.existsByEmail(registerDto.getEmail())) {
            return ESystemStatus.EMAIL_TAKEN;
        }

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
        return ESystemStatus.SUCCESS;
    }

}
