package com.example.pyjachok.controllers;

import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.News;
import com.example.pyjachok.services.EstablishmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/establishments")
public class EstablishmentController {
    private EstablishmentService establishmentService;

    @GetMapping("/activated")
    public List<Establishment> getActivatedEstablishments() {
        return establishmentService.getAllActivated();
    }

    @GetMapping("/desactivated")
    public List<Establishment> getDesactivatedEstablishments() {
        return establishmentService.getAllDesActivated();
    }

    @PostMapping("/activate")


    @GetMapping
    public List<Establishment> getEstablishments() {
        return establishmentService.getAll();
    }

    @GetMapping("/{id}")
    public Establishment getEstablishmentById(@PathVariable int id) {
        return establishmentService.getById(id);
    }

    @GetMapping("/{id}/news")
    public ResponseEntity<List<News>> getNewsOfEstablishment(@PathVariable int id) {
        List<News> newsList = establishmentService.getNewsOfEstablishment(id);
        return ResponseEntity.ok(newsList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEstablishmentsById(@PathVariable int id) {
        establishmentService.deleteEstablishment(id);
        return ResponseEntity.ok("Establishment deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEstablishmentsById(@PathVariable int id, @RequestBody Map<String, Object> requestBody) {
        establishmentService.updateEstablishment(id, requestBody);
        return ResponseEntity.ok("Establishment updated successfully");
    }

    @GetMapping("/user")
    public List<Establishment> getEstablishmentsByUser(Principal principal) {
        String email = principal.getName();
        return establishmentService.getEstablishmentsByUserEmail(email);
    }

    @PostMapping("/{id}/news/add")
    public ResponseEntity<String> addNewToEstablishment(@PathVariable int id, @RequestBody News news) {
        return establishmentService.addNewToEstablishment(id, news);
    }

    @PutMapping("/news/{id}")
    public ResponseEntity<String> updateNewInEstablishment(@PathVariable int id, @RequestBody News updatedNews) {
        return establishmentService.updateNewInEstablishment(id, updatedNews);
    }

    @PutMapping("/{establishmentId}/users/{userId}")
    public ResponseEntity<String> changeEstablishmentUser(@PathVariable int establishmentId, @PathVariable int userId) {
        return establishmentService.changeEstablishmentUser(establishmentId, userId);
    }
    @PutMapping("/activate/{id}")
    public void activiteEstablishment(@PathVariable int id){
        establishmentService.activateEstablishment(id);
    }
    @PutMapping("/desactivate/{id}")
    public void desActiviteEstablishment(@PathVariable int id){
        establishmentService.desActivateEstablishment(id);
    }
}
