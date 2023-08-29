package com.example.pyjachok.dao;

import com.example.pyjachok.models.Analitic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnaliticDAO extends JpaRepository<Analitic,Integer> {

    List<Analitic> getAnaliticByEstablishmentId(int id);
    Analitic findByNameAndDateAndEstablishmentId(String name, String date,int id);
}
