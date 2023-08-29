package com.example.pyjachok.dao;

import com.example.pyjachok.models.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactDAO extends JpaRepository<Contacts,Integer> {

}
