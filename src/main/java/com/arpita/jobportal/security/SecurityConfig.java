package com.arpita.jobportal.security;

import com.arpita.jobportal.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public pages
                        .requestMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/jobs", "/jobs/search", "/jobs/view/**").permitAll()

                        // API endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/jobs/**").permitAll()
                        .requestMatchers("/api/applications/**").authenticated()
                        .requestMatchers("/api/profiles/**").authenticated()

                        // Employer pages
                        .requestMatchers("/employer/**").hasRole("EMPLOYER")

                        // Applicant pages
                        .requestMatchers("/applicant/**").hasRole("APPLICANT")

                        // All other requests need authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // Get the user's role
                            String role = authentication.getAuthorities().stream()
                                    .findFirst()
                                    .map(auth -> auth.getAuthority())
                                    .orElse("");

                            // Redirect based on role
                            if (role.equals("ROLE_EMPLOYER")) {
                                response.sendRedirect("/employer/dashboard");
                            } else if (role.equals("ROLE_APPLICANT")) {
                                response.sendRedirect("/applicant/dashboard");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                );

        return http.build();
    }
}