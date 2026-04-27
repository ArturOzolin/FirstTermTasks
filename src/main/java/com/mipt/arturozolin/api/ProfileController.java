package com.mipt.arturozolin.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

  @GetMapping("/profile")
  public ResponseEntity<Map<String, Object>> profile(Authentication authentication) {
    List<String> authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
    Map<String, Object> body = Map.of(
            "username", authentication.getName(),
            "authorities", authorities);
    return ResponseEntity.ok(body);
  }
}
