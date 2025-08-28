package com.api.shosetsuya.services;

import com.api.shosetsuya.models.dtos.auth.LoginDTO;
import com.api.shosetsuya.models.dtos.auth.RegisterDTO;
import com.api.shosetsuya.models.entities.Users;
import com.api.shosetsuya.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    public void register(RegisterDTO dto) {
        String email = dto.getEmail();
        String username = dto.getUsername();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if(userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("error.register.email.exists",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        if(!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("error.register.confirm-password.mismatch",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        String encodedPassword = encoder.encode(password);
        Users newUser = new Users(email, username, encodedPassword);
        userRepo.save(newUser);
    }

    public Map<String, String> login(LoginDTO dto) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails.getUsername(), false);
        String refreshToken = jwtService.generateToken(userDetails.getUsername(), true);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }
}
