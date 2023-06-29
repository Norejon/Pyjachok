package com.example.pyjachok.controllers;

import com.example.pyjachok.models.Complaints;
import com.example.pyjachok.services.ComplaintsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/complaints")
public class ComplaintsController {
    private ComplaintsService complaintsService;

    @GetMapping
    public List<Complaints> getComplaints() {
        return complaintsService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaints getComplaintById(@PathVariable int id) {
        return complaintsService.getComplaintById(id);
    }

    @PostMapping("/{establishmentId}")
    public ResponseEntity<String> saveComplaint(@PathVariable int establishmentId, Principal principal, @RequestBody Complaints complaints) {
        complaintsService.saveComplaint(principal, establishmentId, complaints);
        return ResponseEntity.ok("Complaint saved successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaintById(@PathVariable int id) {
        complaintsService.deleteComplaint(id);
        return ResponseEntity.ok("Complaint deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateComplaintById(@PathVariable int id, @RequestBody Complaints updateComplaints) {
        complaintsService.updateComplaint(id, updateComplaints);
        return ResponseEntity.ok("Complaint updated successfully.");
    }
}
