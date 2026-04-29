package com.mipt.arturozolin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtUtils jwtUtils;

  public JwtAuthFilter(JwtUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(BEARER_PREFIX.length()).trim();
    if (token.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      Jws<Claims> jws = jwtUtils.parseAndValidate(token);
      Claims claims = jws.getPayload();
      String username = claims.getSubject();
      List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);

      UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(username, null, authorities);
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(auth);
      log.debug("Authenticated user={} via JWT (token={})", username, TokenMasker.mask(token));
    } catch (JwtException ex) {
      SecurityContextHolder.clearContext();
      log.warn("JWT validation failed (token={}): {}", TokenMasker.mask(token), ex.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  @SuppressWarnings("unchecked")
  private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
    Object raw = claims.get("authorities");
    if (raw instanceof List<?> list) {
      return list.stream()
              .map(Object::toString)
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());
    }
    return List.of();
  }
}
