package com.example.pyjachok.controllers;

import com.example.pyjachok.dao.EstablishmentDAO;
import com.example.pyjachok.models.Establishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

    @Autowired
    private EstablishmentDAO establishmentDAO;
    private String uploadDir = "C:/Users/mykol/IdeaProjects/FinishProject/Pyjachok/src/main/resources/static/images";

    @PostMapping("/image")
    public ResponseEntity<String>  uploadImage(@RequestBody MultipartFile file,  @RequestParam("name") String name) {
        System.out.println(name);
        try {
            byte[] imageBytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            System.out.println("Save image to: " + filePath.toAbsolutePath());
            System.out.println(filePath);
            Establishment establishment  = establishmentDAO.getByName(name);
           establishment.setPhoto(uploadDir+fileName);
           establishmentDAO.save(establishment);

            Files.write(filePath, imageBytes);

            return ResponseEntity.ok("Grade saved successfully");
        } catch (IOException e) {
            return ResponseEntity.ok("Failed to save image");
        }
    }
}
