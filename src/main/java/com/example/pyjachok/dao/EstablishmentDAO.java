package com.example.pyjachok.dao;

import com.example.pyjachok.models.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstablishmentDAO extends JpaRepository<Establishment, Integer> {

    Establishment deleteById(int id);

    Establishment getById(int id);

    Establishment getByName(String name);
}
