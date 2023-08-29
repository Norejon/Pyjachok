package com.example.pyjachok.dao;

import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstablishmentDAO extends JpaRepository<Establishment, Integer> {

    Establishment deleteById(int id);

    List<Establishment> findAllByActivatedTrue();
    List<Establishment> findAllByActivatedFalse();

    Establishment getById(int id);

    Establishment getByName(String name);

    List<Establishment> findByUser(User user);
}
