package com.example.pyjachok.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

    private String uploadDir = "C:/Users/mykol/IdeaProjects/FinishProject/images/";

    @PostMapping("/image")
    public ResponseEntity<String>  uploadImage(@RequestBody MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            System.out.println("Save image to: " + filePath.toAbsolutePath());

            Files.write(filePath, imageBytes);

            return ResponseEntity.ok("Grade saved successfully");
        } catch (IOException e) {
            return ResponseEntity.ok("Failed to save image");
        }
    }
}
