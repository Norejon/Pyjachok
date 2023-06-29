package com.example.pyjachok.dao;

import com.example.pyjachok.models.UserEstablishment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEstablishmentDAO extends JpaRepository<UserEstablishment, Integer> {
}
