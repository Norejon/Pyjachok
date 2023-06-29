package com.example.pyjachok.services;

import com.example.pyjachok.dao.EstablishmentDAO;
import com.example.pyjachok.dao.UserDAO;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.News;
import com.example.pyjachok.models.User;
import com.example.pyjachok.models.UserEstablishment;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    public List<Establishment> getAll() {
        return establishmentDAO.findAll();
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


            establishment.setName(name);
            establishment.setPhoto(photo);
            establishment.setType(type);


            establishmentDAO.save(establishment);
        }
    }

    public void deleteEstablishment(int id) {
        Establishment establishment = establishmentDAO.getById(id);
        if (establishment != null) {
            User user = establishment.getUser();
            if (user != null) {
                user.getUserEstablishments().removeIf(ue -> ue.getEstablishment().getId() == id);
                userDAO.save(user);
            }
            establishmentDAO.deleteById(id);
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
        newsService.addNew(news);

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

}
