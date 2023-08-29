package com.example.pyjachok.configs.security;

import com.example.pyjachok.configs.security.filters.JWTFilter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private JWTFilter jwtFilter;

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,"/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/activation/{email}").permitAll()

                .requestMatchers(HttpMethod.PUT, "/establishments/{establishmentId}/users/{userId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/establishments/activate/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/establishments/desactivate/{id}").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.GET, "/grades").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.GET, "/complaints").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/complaints/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/complaints/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/complaints/{id}").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.GET,"/analitic").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/analitic/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/analitic/{id}").hasAuthority("ADMIN")

                .anyRequest().permitAll()
                .and().cors().configurationSource(corsConfigurationSource())
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @SneakyThrows
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://mail.google.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
