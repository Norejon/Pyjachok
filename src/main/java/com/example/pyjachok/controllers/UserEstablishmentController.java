package com.example.pyjachok.controllers;

import com.example.pyjachok.services.UserEstablishmentService;
import com.example.pyjachok.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/establishments")
@AllArgsConstructor
public class UserEstablishmentController {

    private UserService userService;
    private UserEstablishmentService userEstablishmentService;

    @PostMapping
    public ResponseEntity<String> saveEstablishment(@RequestBody Map<String, Object> requestBody, Principal principal) {
        userEstablishmentService.saveEstablishment(requestBody, principal);

        return ResponseEntity.ok("Establishment created successfully.");
    }
    @PostMapping("/{id}/add_favorite")
    public ResponseEntity<String> addEstablishmentToFavorites(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        userService.addEstablishmentToFavorites(username, id);

        return ResponseEntity.ok("Establishment added to favorites successfully");
    }

    @DeleteMapping("/{id}/remove_favorite")
    public ResponseEntity<String> removeEstablishmentFromFavorites(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        userService.removeEstablishmentFromFavorites(username, id);

        return ResponseEntity.ok("Establishment removed from favorites successfully");
    }


}