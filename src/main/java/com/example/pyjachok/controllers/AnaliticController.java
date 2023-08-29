package com.example.pyjachok.controllers;

import com.example.pyjachok.models.Analitic;
import com.example.pyjachok.services.AnaliticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/analitic")
public class AnaliticController {

    private AnaliticService analiticService;

    @GetMapping
    public List<Analitic> getAll(){
        return analiticService.getAnalitics();
    }

    @GetMapping("/{id}")
    public List<Analitic> getById(@PathVariable int id){
        return analiticService.getById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> saveAnalitic(@PathVariable int id, @RequestBody Analitic analitic) {
        return analiticService.saveAnalitic(id, analitic);
    }
}
