package com.example.pyjachok.configs.security.filters;

import com.example.pyjachok.dao.UserDAO;
import com.example.pyjachok.models.User;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private UserDAO userDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer", "");
            String decodedData = Jwts.parser()
                    .setSigningKey("secret".getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            User user = userDAO.findByEmail(decodedData);
            System.out.println("================ " + user.isEnable() + " ++++++++++++++++++");
            if (user != null && user.isEnable()) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword(),
                                user.getAuthorities()
                        ));
                System.out.println(SecurityContextHolder.getContext());
            }
        }
        filterChain.doFilter(request, response);
    }
}
