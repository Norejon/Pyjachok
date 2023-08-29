package com.example.pyjachok.dao;

import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import com.example.pyjachok.models.UserEstablishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserEstablishmentDAO extends JpaRepository<UserEstablishment, Integer> {
    List<UserEstablishment> findByEstablishment(Establishment establishment);
    List<UserEstablishment> findByUser(User user);
    void deleteByUser(@Param("user") User user);

}
