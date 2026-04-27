package com.mipt.arturozolin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.arturozolin.security.JwtAuthFilter;
import com.mipt.arturozolin.security.JwtUtils;
import com.mipt.arturozolin.security.PepperPasswordEncoder;
import com.mipt.arturozolin.security.RestAccessDeniedHandler;
import com.mipt.arturozolin.security.RestAuthenticationEntryPoint;
import com.mipt.arturozolin.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder(SecurityProperties securityProperties) {
    return new PepperPasswordEncoder(new BCryptPasswordEncoder(12), securityProperties.getPassword().getPepper());
  }

  @Bean
  public JwtUtils jwtUtils(SecurityProperties securityProperties) {
    return new JwtUtils(securityProperties);
  }

  @Bean
  public JwtAuthFilter jwtAuthFilter(JwtUtils jwtUtils) {
    return new JwtAuthFilter(jwtUtils);
  }

  @Bean
  public RestAuthenticationEntryPoint restAuthenticationEntryPoint(ObjectMapper objectMapper) {
    return new RestAuthenticationEntryPoint(objectMapper);
  }

  @Bean
  public RestAccessDeniedHandler restAccessDeniedHandler(ObjectMapper objectMapper) {
    return new RestAccessDeniedHandler(objectMapper);
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder.encode("password"))
            .authorities("ROLE_USER")
            .build();

    UserDetails reader = User.builder()
            .username("reader")
            .password(passwordEncoder.encode("password"))
            .authorities("ROLE_USER", "READ_PRIVILEGE")
            .build();

    return new InMemoryUserDetailsManager(user, reader);
  }

  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                     PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new org.springframework.security.authentication.ProviderManager(provider);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                 JwtAuthFilter jwtAuthFilter,
                                                 RestAuthenticationEntryPoint authenticationEntryPoint,
                                                 RestAccessDeniedHandler accessDeniedHandler) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/external/**").permitAll()
                    .requestMatchers("/api/tasks/**").permitAll()
                    .requestMatchers("/api/preferences/**").permitAll()
                    .requestMatchers("/api/favorites/**").permitAll()
                    .requestMatchers("/api/attachments/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/api/v1/profile").hasRole("USER")
                    .requestMatchers("/api/v1/docs").hasAuthority("READ_PRIVILEGE")
                    .requestMatchers("/api/v1/tasks/**").authenticated()
                    .anyRequest().authenticated())
            .exceptionHandling(eh -> eh
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
