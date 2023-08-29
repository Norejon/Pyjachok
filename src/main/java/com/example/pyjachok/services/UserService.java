package com.example.pyjachok.services;

import com.example.pyjachok.dao.*;
import com.example.pyjachok.models.*;
import com.example.pyjachok.models.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
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
    @Autowired
    private UserEstablishmentDAO userEstablishmentDAO;
    @Autowired
    private EstablishmentDAO establishmentDAO;
    @Autowired
    private DrinkerDAO drinkerDAO;
    @Autowired
    private GradesDAO gradesDAO;

    @Autowired
    private Environment env;


    public List<User> getUsers() {
        System.out.println("done");
        return userDAO.findAll();
    }

    public User getUserById(@PathVariable int id) {
        return userDAO.findById(id);
    }

    @Transactional
    public void deleteUserById(int id) {
        User user = userDAO.findById(id);
        if (user != null) {
            List<Establishment> establishments = establishmentDAO.findByUser(user);
            for (Establishment establishment : establishments) {
                establishment.setUser(null);
                establishmentDAO.save(establishment);
            }

            List<Drinker> drinkers = drinkerDAO.findByUser(user);
            for (Drinker drinker : drinkers) {
                drinker.setUser(null);
                drinkerDAO.save(drinker);
            }

            List<Grades> grades = gradesDAO.findByUser(user);
            for (Grades grade : grades) {
                grade.setUser(null);
                gradesDAO.save(grade);
            }

            userDAO.delete(user);
        }
    }

    public void saveUser(@RequestBody UserDTO userDTO) throws MessagingException {
        User user = new User();
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setBirth(userDTO.getBirth());
        user.setGender(userDTO.getGender());
        user.setEnable(false);
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

    public void removeEstablishmentFromFavorites(String username, int establishmentId) {
        User user = userDAO.findByEmail(username);
        if (user == null) {
            return;
        }

        Establishment establishment = establishmentService.getById(establishmentId);
        if (establishment == null) {
            return;
        }

        List<Establishment> favorites = user.getFavorite();
        if (!favorites.contains(establishment)) {
            return;
        }

        favorites.remove(establishment);
        user.setFavorite(favorites);
        userDAO.save(user);
    }

    public void updateUserById(int id, UserDTO updatedUser) {
        User user = userDAO.findById(id);
        user.setNickname(updatedUser.getNickname());
        user.setBirth(updatedUser.getBirth());
        user.setGender(updatedUser.getGender());
        userDAO.save(user);
    }

    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDTO.getEmail(), userDTO.getPassword());
        System.out.println(usernamePasswordAuthenticationToken.getCredentials());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println(authenticate);
        String jwtDecodingKey = env.getProperty("jwt.decodingkey");
        System.out.println("-------"+jwtDecodingKey+"----------");
        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS512, jwtDecodingKey.getBytes(StandardCharsets.UTF_8))
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

    public User getUserByNickname(String nickname) {
        return userDAO.findByNickname(nickname);
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
