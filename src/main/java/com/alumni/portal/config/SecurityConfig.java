package com.alumni.portal.config;

import com.alumni.portal.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles(Role.ADMIN.name())
                .build();

        UserDetails accountant = User.builder()
                .username("accountant")
                .password(passwordEncoder.encode("accountant123"))
                .roles(Role.INVOICING_USER.name())
                .build();

        UserDetails alumni = User.builder()
                .username("alumni_user")
                .password(passwordEncoder.encode("alumni123"))
                .roles(Role.ALUMNI_CONTACT.name())
                .build();

        return new InMemoryUserDetailsManager(admin, accountant, alumni);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST APIs to allow easy Postman & curl testing
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .authorizeHttpRequests(auth -> auth
                // Static resources & public landing pages
                .requestMatchers("/", "/login", "/signin", "/dashboard", "/dashboard/**", "/index.html", "/css/**", "/js/**", "/images/**", "/favicon.ico", "/h2-console/**").permitAll()
                // Public read-only lookups or search
                .requestMatchers(HttpMethod.GET, "/api/alumni/search", "/api/campaigns", "/api/products", "/api/contacts").permitAll()
                // Donation contributions - accessible by Alumni, Invoicing User & Admin
                .requestMatchers(HttpMethod.POST, "/api/donations").hasAnyRole(Role.ADMIN.name(), Role.INVOICING_USER.name(), Role.ALUMNI_CONTACT.name())
                .requestMatchers(HttpMethod.GET, "/api/donations/**").hasAnyRole(Role.ADMIN.name(), Role.INVOICING_USER.name(), Role.ALUMNI_CONTACT.name())
                .requestMatchers(HttpMethod.POST, "/api/alumni").hasAnyRole(Role.ADMIN.name(), Role.INVOICING_USER.name(), Role.ALUMNI_CONTACT.name())
                .requestMatchers(HttpMethod.GET, "/api/alumni/**").hasAnyRole(Role.ADMIN.name(), Role.INVOICING_USER.name(), Role.ALUMNI_CONTACT.name())
                // Invoicing, Procurement & Reporting: INVOICING_USER or ADMIN
                .requestMatchers("/api/reports/**").hasAnyRole(Role.ADMIN.name(), Role.INVOICING_USER.name())
                .requestMatchers("/api/vendor-bills/**", "/api/purchase-orders/**", "/api/payments/**").hasAnyRole(Role.ADMIN.name(), Role.INVOICING_USER.name())
                .requestMatchers("/api/chart-of-accounts/**", "/api/journals/**", "/api/journal-entries/**").hasAnyRole(Role.ADMIN.name(), Role.INVOICING_USER.name())
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
