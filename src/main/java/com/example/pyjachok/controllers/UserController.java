package com.example.pyjachok.controllers;

import com.example.pyjachok.models.User;
import com.example.pyjachok.models.dto.UserDTO;
import com.example.pyjachok.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO) throws MessagingException {
        userService.saveUser(userDTO);
        return ResponseEntity.ok("User saved successfully.");
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable int id, @RequestBody User updatedUser) {
        userService.updateUserById(id, updatedUser);
        return ResponseEntity.ok("User updated successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO clientUserDTO) {
        return userService.login(clientUserDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Отримання токену з заголовку Authorization
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            System.out.println("Ви успішно вийшли з системи!");
            return new ResponseEntity<>("logout", HttpStatus.OK);
        }

        System.out.println("Немає доступного токену для виходу з системи.");
        return new ResponseEntity<>("no token available", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/activation/{email}")
    public void activiteUser(@PathVariable String email){
        userService.activateUser(email);
    }
}

