package com.example.pyjachok.services;

import com.example.pyjachok.dao.*;
import com.example.pyjachok.models.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EstablishmentService {

    private EstablishmentDAO establishmentDAO;
    private UserDAO userDAO;
    private NewsService newsService;
    private UserEstablishmentDAO userEstablishmentDAO;
    private DrinkerDAO drinkerDAO;
    private ContactDAO contactDAO;
    private ContactsService contactsService;


    public List<Establishment> getAll() {
        List<Establishment> establishments = establishmentDAO.findAll();
        establishments.stream().forEach(Establishment::calculateRating);
        return establishments;
    }

    public void saveEstablishment(Establishment establishment) {
        User user = establishment.getUser();
        Establishment savedEstablishment = establishmentDAO.save(establishment);

        UserEstablishment userEstablishment = new UserEstablishment();
        userEstablishment.setUser(user);
        userEstablishment.setEstablishment(savedEstablishment);

        user.getUserEstablishments().add(userEstablishment);
        userDAO.save(user);
    }

    public void updateEstablishment(int id, Map<String, Object> requestBody) {
        Establishment establishment = establishmentDAO.getById(id);
        if (establishment != null) {
            String name = (String) requestBody.get("name");
            String photo = (String) requestBody.get("photo");
            String type = (String) requestBody.get("type");
            List<String> tags = (List<String>) requestBody.get("tags");
            int midle_check = (int) requestBody.getOrDefault("midle_check", 0);

            establishment.setName(name);
            establishment.setPhoto(photo);
            establishment.setType(type);
            establishment.setTags(tags);
            establishment.setMidle_check(midle_check);
            establishment.setSchedule((String) requestBody.get("schedule"));
            establishment.setLocation((String) requestBody.get("location"));

            System.out.println(establishment.getContacts());
            Contacts contacts = establishment.getContacts();
            if (contacts == null) {
                contacts = new Contacts();
                establishment.setContacts(contacts);
            }

            contacts.setWebsite((String) ((Map<String, Object>) requestBody.get("contacts")).get("website"));
            contacts.setPhone((String) ((Map<String, Object>) requestBody.get("contacts")).get("phone"));
            contacts.setEmail((String) ((Map<String, Object>) requestBody.get("contacts")).get("email"));
            contacts.setTelegram((String) ((Map<String, Object>) requestBody.get("contacts")).get("telegram"));
            contacts.setInstagram((String) ((Map<String, Object>) requestBody.get("contacts")).get("instagram"));
            contacts.setOthers((String) ((Map<String, Object>) requestBody.get("contacts")).get("others"));

            contacts.setEstablishment(establishment);

            contactsService.saveContacts(contacts);

            establishmentDAO.save(establishment);
        }
    }
    public List<News> getNewsOfEstablishment(int id){
        Establishment establishment = establishmentDAO.getById(id);
        if(establishment.getNews().size()==0){
            return Collections.emptyList();
        }
        return establishment.getNews();
    }

    @Transactional
    public void deleteEstablishment(int id) {
        Establishment establishment = establishmentDAO.getById(id);
        if (establishment != null) {
            List<UserEstablishment> userEstablishments = userEstablishmentDAO.findByEstablishment(establishment);
            userEstablishments.forEach(userEstablishmentDAO::delete);

            List<User> users = userDAO.findByFavorite(establishment);
            users.forEach(user -> user.getFavorite().remove(establishment));

            List<Grades> gradesList = establishment.getGradesList();
            gradesList.forEach(grades -> grades.setEstablishment(null));
            gradesList.clear();

            Contacts contacts = establishment.getContacts();
            if (contacts != null) {
                establishment.setContacts(null);
                contacts.setEstablishment(null);
                contactDAO.delete(contacts);
            }

            establishmentDAO.delete(establishment);
        }
    }
    public Establishment getById(int id) {
        List<Establishment> establishments = getAll();
        return establishments.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Establishment> getEstablishmentsByUserEmail(String email) {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            List<UserEstablishment> userEstablishments = user.getUserEstablishments();
            return userEstablishments.stream()
                    .map(UserEstablishment::getEstablishment)
                    .peek(Establishment::calculateRating)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Establishment getEstablishmentByName(String name) {
        return establishmentDAO.getByName(name);
    }

    public ResponseEntity<String> addNewToEstablishment(int id, News news) {
        Establishment establishment = establishmentDAO.getById(id);

        if (establishment == null) {
            return ResponseEntity.notFound().build();
        }

        news.setEstablishment(establishment);

        List<News> newsList = establishment.getNews();
        newsList.add(news);

        establishmentDAO.save(establishment);

        return ResponseEntity.ok("News added to establishment successfully");
    }
    public ResponseEntity<String> updateNewInEstablishment(int id, News updatedNews) {
        Establishment establishment = establishmentDAO.getById(id);
        if (establishment == null) {
            return ResponseEntity.notFound().build();
        }

        int newsId = updatedNews.getId();

        News existingNews = newsService.getById(newsId);

        if (existingNews == null) {
            return ResponseEntity.notFound().build();
        }


        existingNews.setText(updatedNews.getText());
        existingNews.setPhoto(updatedNews.getPhoto());
        existingNews.setType(updatedNews.getType());

        newsService.updateNew(newsId, existingNews);

        return ResponseEntity.ok("News updated in establishment successfully");
    }

    public ResponseEntity<String> changeEstablishmentUser(int establishmentId, int userId) {
        Establishment establishment = establishmentDAO.getById(establishmentId);
        User newUser = userDAO.getById(userId);

        if (establishment == null || newUser == null) {
            return ResponseEntity.notFound().build();
        }

        User oldUser = establishment.getUser();

        if (oldUser != null) {
            List<UserEstablishment> userEstablishments = oldUser.getUserEstablishments();
            UserEstablishment userEstablishmentToRemove = userEstablishments.stream()
                    .filter(ue -> ue.getEstablishment().getId() == establishmentId)
                    .findFirst()
                    .orElse(null);
            if (userEstablishmentToRemove != null) {
                userEstablishments.remove(userEstablishmentToRemove);
                userDAO.save(oldUser);
            }
        }

        establishment.setUser(newUser);
        establishmentDAO.save(establishment);

        return ResponseEntity.ok("Establishment user changed successfully");
    }

    public void activateEstablishment(int id) {
        Establishment establishmentForActivite = establishmentDAO.getById(id);
        establishmentForActivite.setActivated(true);
        establishmentDAO.save(establishmentForActivite);
    }
    public void desActivateEstablishment(int id) {
        Establishment establishmentForDesActivite = establishmentDAO.getById(id);
        establishmentForDesActivite.setActivated(false);
        establishmentDAO.save(establishmentForDesActivite);
    }

    public List<Establishment> getAllActivated() {
        List<Establishment> establishments = establishmentDAO.findAllByActivatedTrue();
        establishments.stream().forEach(Establishment::calculateRating);
        return establishments;
    }

    public List<Establishment> getAllDesActivated() {
        List<Establishment> establishments = establishmentDAO.findAllByActivatedFalse();
        establishments.stream().forEach(Establishment::calculateRating);
        return establishments;
    }
}
