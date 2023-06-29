package com.example.pyjachok.services;

import com.example.pyjachok.dao.UserDAO;
import com.example.pyjachok.dao.UserEstablishmentDAO;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import com.example.pyjachok.models.UserEstablishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserEstablishmentService {

    private UserEstablishmentDAO userEstablishmentDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private EstablishmentService establishmentService;

    public UserEstablishmentService(UserEstablishmentDAO userEstablishmentDAO) {
        this.userEstablishmentDAO = userEstablishmentDAO;
    }

    public ResponseEntity<String> saveEstablishment(Map<String, Object> requestBody, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        String name = (String) requestBody.get("name");
        String photo = (String) requestBody.get("photo");
        String type = (String) requestBody.get("type");
        String tags = (String) requestBody.get("tags");
        int rating = (int) requestBody.getOrDefault("rating", 0);
        int midle_check = (int) requestBody.getOrDefault("midle_check", 0);
        String registration_date = (String) requestBody.get("registration_date");
        String location = (String) requestBody.get("location");
        String schedule = (String) requestBody.get("schedule");
        boolean activated = (boolean) requestBody.getOrDefault("activated", false);

        Establishment establishment = new Establishment();
        establishment.setName(name);
        establishment.setPhoto(photo);
        establishment.setType(type);
        establishment.setTags(tags);
        establishment.setRating(rating);
        establishment.setMidle_check(midle_check);
        establishment.setRegistration_date(registration_date);
        establishment.setLocation(location);
        establishment.setSchedule(schedule);
        establishment.setActivated(activated);
        establishment.setUser(user);

        establishmentService.saveEstablishment(establishment);

        UserEstablishment userEstablishment = UserEstablishment.builder()
                .user(user)
                .establishment(establishment)
                .build();
        userEstablishmentDAO.save(userEstablishment);

        return ResponseEntity.ok("Establishment created successfully.");
    }

    public List<Establishment> getEstablishmentsByUserId(int userId) {
        User user = userDAO.findById(userId);
        List<UserEstablishment> userEstablishments = user.getUserEstablishments();
        return userEstablishments.stream()
                .map(UserEstablishment::getEstablishment)
                .collect(Collectors.toList());
    }
}