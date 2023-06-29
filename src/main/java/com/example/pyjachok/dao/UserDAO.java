package com.example.pyjachok.dao;

import com.example.pyjachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {

    User findById(int id);

    User deleteById(int id);

    User findByUsername(String username);

    User findByEmail(String email);

}
