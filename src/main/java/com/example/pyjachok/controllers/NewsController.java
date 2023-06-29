package com.example.pyjachok.controllers;

import com.example.pyjachok.models.News;
import com.example.pyjachok.services.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private NewsService newsService;

    @GetMapping
    public List<News> getNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public News getNewById(@PathVariable int id) {
        return newsService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNewById(@PathVariable int id) {
        newsService.deleteNew(id);
        return ResponseEntity.ok("New was successfully deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateNewById(@PathVariable int id, @RequestBody News updatedNew) {
        newsService.updateNew(id, updatedNew);
        return ResponseEntity.ok("New was successfully updated");
    }
}
