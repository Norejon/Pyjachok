package com.example.pyjachok.services;

import com.example.pyjachok.dao.UserDAO;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import com.example.pyjachok.models.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@NoArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EstablishmentService establishmentService;
    @Autowired
    private MailService mailService;

    public List<User> getUsers() {
        System.out.println("done");
        return userDAO.findAll();
    }

    public User getUserById(@PathVariable int id) {
        return userDAO.findById(id);
    }

    public User deleteUserById(@PathVariable int id) {
        return userDAO.deleteById(id);
    }

    public void saveUser(@RequestBody UserDTO userDTO) throws MessagingException {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDAO.save(user);
        mailService.send(userDTO);
    }

    public void addEstablishmentToFavorites(String username, int establishmentId) {
        User user = userDAO.findByEmail(username);
        if (user == null) {
            return;
        }

        Establishment establishment = establishmentService.getById(establishmentId);
        if (establishment == null) {
            return;
        }

        List<Establishment> favorites = user.getFavorite();
        if (favorites.contains(establishment)) {
            return;
        }

        favorites.add(establishment);
        user.setFavorite(favorites);
        userDAO.save(user);
    }

    public void updateUserById(int id, User updatedUser) {
        updatedUser.setId(id);
        userDAO.save(updatedUser);
    }

    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDTO.getEmail(), userDTO.getPassword());
        System.out.println(usernamePasswordAuthenticationToken.getCredentials());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println(authenticate);
        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS512, "secret".getBytes(StandardCharsets.UTF_8))
                    .compact();
            System.out.println(jwtToken);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + jwtToken);
            System.out.println(httpHeaders);
            System.out.println("Логінація - успішна!");
            return new ResponseEntity<>("login", httpHeaders, HttpStatus.OK);
        }
        System.out.println("Провал логінації...");
        return new ResponseEntity<>("bad credentials", HttpStatus.FORBIDDEN);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User byEmail = userDAO.findByEmail(email);
        return byEmail;
    }

    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public ResponseEntity<String> activateUser(String email) {
        User userForActivite = userDAO.findByEmail(email);
        if (userForActivite.isEnable() == true) {
            return ResponseEntity.ok("Your account is already activated.");
        } else {
            userForActivite.setEnable(true);
            userDAO.save(userForActivite);
            return ResponseEntity.ok("Your account has been successfully activated.");
        }
    }

}
