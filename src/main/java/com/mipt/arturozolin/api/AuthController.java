package com.mipt.arturozolin.api;

import com.mipt.arturozolin.dto.LoginRequest;
import com.mipt.arturozolin.dto.LoginResponse;
import com.mipt.arturozolin.security.JwtUtils;
import com.mipt.arturozolin.security.TokenMasker;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    List<String> authorities = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

    String token = jwtUtils.generate(auth.getName(), authorities);
    log.info("Login successful: user={} authorities={} token={}",
            auth.getName(), authorities, TokenMasker.mask(token));

    return ResponseEntity.ok(new LoginResponse(token, "Bearer", jwtUtils.getTtlSeconds()));
  }
}
