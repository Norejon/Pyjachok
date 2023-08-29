package com.example.pyjachok.dao;

import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.Grades;
import com.example.pyjachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradesDAO extends JpaRepository<Grades, Integer> {
    Grades deleteById(int id);
    Grades findById(int id);
    Grades findByEstablishment(Establishment establishment);
    List<Grades> findByUser(User user);
    Grades findByUserAndEstablishment(User user, Establishment establishment);

}
