package com.example.taskmanager.controller;

import com.example.taskmanager.config.JwtService;
import com.example.taskmanager.dto.*;
import com.example.taskmanager.model.Operation;
import com.example.taskmanager.model.RefreshToken;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.OperationRepository;
import com.example.taskmanager.repository.RefreshTokenRepository;
import com.example.taskmanager.repository.UserRepository;
import io.jsonwebtoken.lang.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OperationRepository operationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            OperationRepository operationRepository,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.operationRepository = operationRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Operation userRole = operationRepository.findByRole("ROLE_USER");
        User newUser = new User(
                request.getUsername(),
                request.getFullName(),
                passwordEncoder.encode(request.getPassword()),
                Collections.of(userRole)
        );
        userRepository.save(newUser);

        CustomUserDetails userDetails = new CustomUserDetails(newUser);
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String password = request.getPassword();
        String username = request.getUsername();
        try {
            UserDetails principal = (UserDetails) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)).getPrincipal();
            String accessToken = jwtService.generateToken(principal);

            RefreshToken byToken = refreshTokenRepository.findByUserUsername(username);
            String refreshToken = UUID.randomUUID().toString();
            if (byToken == null) {

                RefreshToken refreshTokenEntity = new RefreshToken();
                refreshTokenEntity.setUser(userRepository.findByUsername(principal.getUsername()));
                refreshTokenEntity.setToken(refreshToken);
                refreshTokenEntity.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));

                refreshTokenRepository.save(refreshTokenEntity);

            } else {
                byToken.setToken(refreshToken);
                byToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
                refreshTokenRepository.save(byToken);
            }

            return ResponseEntity.ok().body(new Tokens(accessToken, refreshToken));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        RefreshToken byToken = refreshTokenRepository.findByToken(refreshToken);
        if (byToken != null && byToken.getExpiryDate().isAfter(Instant.now())) {
            User user = byToken.getUser();
            String newAccessToken = jwtService.generateToken(new CustomUserDetails(user));
            byToken.setToken(UUID.randomUUID().toString());
            byToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
            refreshTokenRepository.save(byToken);
            return ResponseEntity.ok().body(new Tokens(newAccessToken, byToken.getToken()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Refresh token not found");
        }

        refreshTokenRepository.delete(token);

        return ResponseEntity.ok("Logged out successfully");
    }

}
