package com.example.pyjachok.services;

import com.example.pyjachok.dao.NewsDAO;
import com.example.pyjachok.models.News;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
public class NewsService {

    private NewsDAO newsDAO;

    public void addNew(@RequestBody News news) {
        newsDAO.save(news);
    }

    public void deleteNew(@PathVariable int id) {
        newsDAO.deleteById(id);
    }

    public List<News> getAllNews() {
        return newsDAO.findAll();
    }

    public News getById(@PathVariable int id) {
        return newsDAO.findById(id);
    }

    public void updateNew(int id, News updatedNew) {
        News existingNew = newsDAO.findById(id);
        if (existingNew != null) {
            existingNew.setTitle(updatedNew.getTitle());
            existingNew.setText(updatedNew.getText());
            existingNew.setPhoto(updatedNew.getPhoto());
            existingNew.setType(updatedNew.getType());

            newsDAO.save(existingNew);
        }
    }
}
