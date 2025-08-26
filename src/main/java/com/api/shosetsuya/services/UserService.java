package com.api.shosetsuya.services;

import com.api.shosetsuya.models.dtos.users.LoginDTO;
import com.api.shosetsuya.models.dtos.users.RegisterDTO;
import com.api.shosetsuya.models.entities.Users;
import com.api.shosetsuya.repositories.UserRepo;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    public void register(RegisterDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if(!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("error.register.confirm-password.mismatch",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        String encodedPassword = encoder.encode(password);
        Users newUser = new Users(username, encodedPassword);
        userRepo.save(newUser);
    }

    public String login(LoginDTO dto) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        return jwtService.generateToken(dto.getUsername());
    }
}
