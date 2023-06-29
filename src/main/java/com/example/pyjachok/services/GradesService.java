package com.example.pyjachok.services;

import com.example.pyjachok.dao.GradesDAO;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.Grades;
import com.example.pyjachok.models.News;
import com.example.pyjachok.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GradesService {

    private GradesDAO gradesDAO;
    private UserService userService;
    private EstablishmentService establishmentService;

    public void addGrade(int establishmentId, Map<String, Object> requestBody, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        Establishment establishment = establishmentService.getById(establishmentId);

        if (user == null || establishment == null) {
            System.out.println("User or establishment not found");
            return;
        }

        int grade = (int) requestBody.getOrDefault("grade", 0);
        String text = (String) requestBody.getOrDefault("text", "");

        Grades gradeObject = new Grades();
        gradeObject.setGrade(grade);
        gradeObject.setText(text);
        gradeObject.setUser(user);
        gradeObject.setEstablishment(establishment);

        gradesDAO.save(gradeObject);
        System.out.println("Grade is successfully added");
    }

    public void deleteGrade(@PathVariable int id) {
        gradesDAO.deleteById(id);
    }

    public List<Grades> getAllGrades() {
        return gradesDAO.findAll();
    }

    public Grades getById(@PathVariable int id) {
        return gradesDAO.findById(id);
    }

    public void updateGradeById(int id, Map<String, Object> gradeMap, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        if (user != null) {
            Grades grade = gradesDAO.findById(id);
            if (grade != null) {

                int newGrade = (int) gradeMap.getOrDefault("grade", grade.getGrade());
                String newText = (String) gradeMap.getOrDefault("text", grade.getText());


                grade.setGrade(newGrade);
                grade.setText(newText);
                gradesDAO.save(grade);
                System.out.println("Grade successfully updated");
            } else {
                System.out.println("Grade not found");
            }
        } else {
            System.out.println("User not found");
        }
    }
}
