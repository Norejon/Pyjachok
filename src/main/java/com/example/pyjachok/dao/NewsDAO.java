package com.example.pyjachok.dao;

import com.example.pyjachok.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsDAO extends JpaRepository<News, Integer> {

    News deleteById(int id);

    News findById(int id);
}
