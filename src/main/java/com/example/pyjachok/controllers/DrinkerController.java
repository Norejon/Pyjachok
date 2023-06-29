package com.example.pyjachok.controllers;

import com.example.pyjachok.models.Drinker;
import com.example.pyjachok.models.dto.DrinkerDTO;
import com.example.pyjachok.services.DrinkerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/drinkers")
public class DrinkerController {
    private DrinkerService drinkerService;

    @GetMapping
    public List<Drinker> getDrinker() {
        return drinkerService.getAllDrinkers();
    }

    @GetMapping("/{id}")
    public Drinker getDrinkerById(@PathVariable int id) {
        return drinkerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<String> saveDrinker(@RequestBody DrinkerDTO drinkerDTO, Principal principal) {
        drinkerService.saveDrinker(drinkerDTO, principal.getName());
        return ResponseEntity.ok("Drinker saved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDrinkerById(@PathVariable int id) {
        drinkerService.deleteDrinker(id);
        return ResponseEntity.ok("Drinker deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDrinkerById(@PathVariable int id, @RequestBody Drinker updatedDrinker) {
        drinkerService.updateDrinker(id, updatedDrinker);
        return ResponseEntity.ok("Drinker updated successfully.");
    }
}
