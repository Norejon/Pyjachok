package com.example.pyjachok.dao;

import com.example.pyjachok.models.Complaints;
import com.example.pyjachok.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintsDAO extends JpaRepository<Complaints, Integer> {

    Complaints deleteById(int id);

    Complaints findById(int id);
}
