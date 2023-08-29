package com.example.pyjachok.dao;

import com.example.pyjachok.models.Drinker;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrinkerDAO extends JpaRepository<Drinker, Integer> {

    Drinker deleteById(int id);

    Drinker findById(int id);
    List<Drinker> findByUser(User user);
    List<Drinker> findByEstablishment(Establishment establishment);
}
