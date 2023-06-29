package com.example.pyjachok.dao;

import com.example.pyjachok.models.Grades;
import com.example.pyjachok.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradesDAO extends JpaRepository<Grades, Integer> {
    Grades deleteById(int id);

    Grades findById(int id);
}
