package com.api.shosetsuya.services;

import com.api.shosetsuya.models.dtos.users.LoginDTO;
import com.api.shosetsuya.models.dtos.users.RegisterDTO;
import com.api.shosetsuya.models.entities.Users;
import com.api.shosetsuya.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
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

    public boolean register(RegisterDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if(!password.equals(confirmPassword)) {
            return false;
        }

        String encodedPassword = encoder.encode(password);
        Users newUser = new Users(username, encodedPassword);
        userRepo.save(newUser);

        return true;
    }

    public String login(LoginDTO dto) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(dto.getUsername());
        }

        return "Login Failed!";
    }
}
