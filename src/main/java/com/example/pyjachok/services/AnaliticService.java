package com.example.pyjachok.services;

import com.example.pyjachok.dao.AnaliticDAO;
import com.example.pyjachok.models.Analitic;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class AnaliticService {
    private AnaliticDAO analiticDAO;

    public List<Analitic> getAnalitics(){
        return analiticDAO.findAll();
    }

    public ResponseEntity<String> saveAnalitic(@PathVariable int id, @RequestBody Analitic analitic) {
        LocalDate currentDate = LocalDate.now();
        analitic.setDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        analitic.setEstablishmentId(id);
        Analitic existingAnalitig= analiticDAO.findByNameAndDateAndEstablishmentId(analitic.getName(), analitic.getDate(),id);
        if (existingAnalitig==null) {
            analiticDAO.save(analitic);
        }else {
            return ResponseEntity.ok("Analitic already exist.");

        }
        return ResponseEntity.ok("Analitic saved successfully.");
    }
    public List<Analitic> getById(@PathVariable int id){
        List<Analitic> analitics =analiticDAO.getAnaliticByEstablishmentId(id);;
        return analitics;
    }
}
