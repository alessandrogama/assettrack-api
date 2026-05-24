package com.assettrack.assettrack_api.adapters.http.controller;

import com.assettrack.assettrack_api.application.dto.request.LoginRequest;
import com.assettrack.assettrack_api.application.dto.response.LoginResponse;
import com.assettrack.assettrack_api.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "JWT token login and renewal.")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "\n" +
            "Authenticates the user and returns JWT tokens.")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        String role = user.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(LoginResponse.of(
                accessToken, refreshToken, jwtService.getAccessTokenExpiration(),
                user.getUsername(), role
        ));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renew the access token using the refresh token.")
    public ResponseEntity<LoginResponse> refresh(@RequestBody String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            return ResponseEntity.status(401).build();
        }

        String newAccessToken = jwtService.generateAccessToken(user);
        String role = user.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(LoginResponse.of(
                newAccessToken, refreshToken, jwtService.getAccessTokenExpiration(),
                user.getUsername(), role
        ));
    }
}
