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

                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                .requestMatchers(HttpMethod.POST,"/users/logout").authenticated()
                .requestMatchers(HttpMethod.GET, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/users/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/users/activation/{email}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/users/{id}").authenticated()

                .requestMatchers(HttpMethod.PUT, "/establishments/{establishmentId}/users/{userId}").permitAll()
                .requestMatchers(HttpMethod.POST, "/establishments").permitAll()
                .requestMatchers(HttpMethod.POST, "/establishments/{id}/news/add").permitAll()
                .requestMatchers(HttpMethod.POST, "/establishments/{id}/add_favorite").permitAll()
                .requestMatchers(HttpMethod.GET, "/establishments").permitAll()
                .requestMatchers(HttpMethod.GET, "/establishments/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/establishments/user").permitAll()
                .requestMatchers(HttpMethod.PUT, "/establishments/news/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/establishments/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT,"/establishments/activite/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT,"/establishments/desactivite/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/establishments/{id}").permitAll()

                .requestMatchers(HttpMethod.POST, "/drinkers").authenticated()
                .requestMatchers(HttpMethod.GET, "/drinkers").authenticated()
                .requestMatchers(HttpMethod.GET, "/drinkers/{id}").authenticated()
                .requestMatchers(HttpMethod.PUT, "/drinkers/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/drinkers/{id}").authenticated()

                .requestMatchers(HttpMethod.POST, "/grades/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/grades").authenticated()
                .requestMatchers(HttpMethod.GET, "/grades/{id}").authenticated()
                .requestMatchers(HttpMethod.PUT, "/grades/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/grades/{id}").authenticated()

                .requestMatchers(HttpMethod.GET, "/news").permitAll()
                .requestMatchers(HttpMethod.GET, "/news/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/news/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/news/{id}").permitAll()

                .requestMatchers(HttpMethod.POST,"/complaints/{establishmentId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/complaints").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/complaints/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/complaints/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/complaints/{id}").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
        configuration.setAllowedMethods(Arrays.asList("GET", HttpMethod.POST.name()));
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedOrigin("https://mail.google.com");
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
