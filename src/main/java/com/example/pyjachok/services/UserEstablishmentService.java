package com.example.pyjachok.services;

import com.example.pyjachok.dao.UserDAO;
import com.example.pyjachok.dao.UserEstablishmentDAO;
import com.example.pyjachok.models.Contacts;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import com.example.pyjachok.models.UserEstablishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private ContactsService contactsService;

    public UserEstablishmentService(UserEstablishmentDAO userEstablishmentDAO) {
        this.userEstablishmentDAO = userEstablishmentDAO;
    }

    public ResponseEntity<String> saveEstablishment(Map<String, Object> requestBody, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        LocalDate currentDate = LocalDate.now();

        String name = (String) requestBody.get("name");
        String photo = (String) requestBody.get("photo");
        String type = (String) requestBody.get("type");
        List<String> tags = (List<String>) requestBody.get("tags");
        int rating = (int) requestBody.getOrDefault("rating", 0);
        int midle_check = (int) requestBody.getOrDefault("midle_check", 0);
        String registration_date = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String location = (String) requestBody.get("location");
        String schedule = (String) requestBody.get("schedule");
        boolean activated = (boolean) requestBody.getOrDefault("activated", false);
        Contacts contacts = new Contacts();
        String website = (String) ((Map<String, Object>) requestBody.get("contacts")).get("website");
        String phone = (String) ((Map<String, Object>) requestBody.get("contacts")).get("phone");
        String contactEmail = (String) ((Map<String, Object>) requestBody.get("contacts")).get("email");
        String telegram = (String) ((Map<String, Object>) requestBody.get("contacts")).get("telegram");
        String instagram = (String) ((Map<String, Object>) requestBody.get("contacts")).get("instagram");
        String others = (String) ((Map<String, Object>) requestBody.get("contacts")).get("others");
        System.out.println(telegram);

        contacts.setWebsite(website);
        contacts.setPhone(phone);
        contacts.setEmail(contactEmail);
        contacts.setTelegram(telegram);
        contacts.setInstagram(instagram);
        contacts.setOthers(others);

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

        System.out.println(requestBody);
        contactsService.saveContacts(contacts);
        establishment.setContacts(contacts);
        contacts.setEstablishment(establishment);

        establishmentService.saveEstablishment(establishment);

        return ResponseEntity.ok("Establishment created successfully.");
    }

    public List<Establishment> getEstablishmentsByUserId(int userId) {
        User user = userDAO.findById(userId);
        List<UserEstablishment> userEstablishments = user.getUserEstablishments();
        return userEstablishments.stream()
                .map(UserEstablishment::getEstablishment)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> saveImage(MultipartFile file){
        return ResponseEntity.ok("Image saved successfully.");
    }

}