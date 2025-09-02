package com.api.shosetsuya.services;

import com.api.shosetsuya.helpers.exceptions.ResourceNotFoundException;
import com.api.shosetsuya.models.dtos.auth.LoginDTO;
import com.api.shosetsuya.models.dtos.auth.RegisterDTO;
import com.api.shosetsuya.models.entities.TranslationGroupMember;
import com.api.shosetsuya.models.entities.User;
import com.api.shosetsuya.models.enums.TranslationGroupRole;
import com.api.shosetsuya.models.enums.UserRole;
import com.api.shosetsuya.repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    public User findById(UUID userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage(
                                "error.user.not-found",
                                null,
                                LocaleContextHolder.getLocale()
                        )
                ));
    }

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
        User newUser = new User(email, username, encodedPassword);
        newUser.setRoles(new HashSet<>(List.of(UserRole.USER)));
        userRepo.save(newUser);
    }

    public Map<String, String> login(LoginDTO dto) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails.getUsername(), false, userDetails.getAuthorities());
        String refreshToken = jwtService.generateToken(userDetails.getUsername(), true, userDetails.getAuthorities());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Set<String> getCurrentUserRoles() {
        Set<UserRole> appRoles = getCurrentUser().getRoles();
        Set<TranslationGroupRole> transGroupRoles = getCurrentUser().getGroupMembers()
                .stream()
                .flatMap(tgMember -> tgMember.getRoles().stream())
                .collect(Collectors.toSet());

        return Stream.concat(
                appRoles.stream().map(Enum::name),
                transGroupRoles.stream().map(role -> "TG_" + role.name())
        ).collect(Collectors.toSet());
    }
}
