package com.example.pyjachok.dao;

import com.example.pyjachok.models.Drinker;
import com.example.pyjachok.models.Grades;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkerDAO extends JpaRepository<Drinker, Integer> {

    Drinker deleteById(int id);

    Drinker findById(int id);
}
