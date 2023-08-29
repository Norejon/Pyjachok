package com.example.pyjachok.configs.security.filters;

import com.example.pyjachok.dao.UserDAO;
import com.example.pyjachok.models.User;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
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

    private final Environment env;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String jwtDecodingKey = env.getProperty("jwt.decodingkey");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer", "");
            String decodedData = Jwts.parser()
                    .setSigningKey(jwtDecodingKey.getBytes(StandardCharsets.UTF_8))
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
        response.setHeader("Access-Control-Allow-Origin","http://localhost:4200");
        filterChain.doFilter(request, response);
    }
}
