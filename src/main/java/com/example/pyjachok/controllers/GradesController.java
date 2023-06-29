package com.example.pyjachok.controllers;


import com.example.pyjachok.models.Grades;
import com.example.pyjachok.services.GradesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/grades")
public class GradesController {

    private GradesService gradesService;


    @GetMapping
    public List<Grades> getGrades() {
        return gradesService.getAllGrades();
    }

    @GetMapping("/{id}")
    public Grades getGradeById(@PathVariable int id) {
        return gradesService.getById(id);
    }


    @DeleteMapping("/{id}")
    public void deleteGradeById(@PathVariable int id) {
        gradesService.deleteGrade(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> saveGradeOfEstablishment(@PathVariable int id, @RequestBody Map<String, Object> requestBody, Principal principal) {
        gradesService.addGrade(id, requestBody, principal);
        return ResponseEntity.ok("Grade saved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGradeById(@PathVariable int id, @RequestBody Map<String, Object> gradeMap, Principal principal) {
        gradesService.updateGradeById(id, gradeMap, principal);
        return ResponseEntity.ok("Grade update successfully");
    }
}
