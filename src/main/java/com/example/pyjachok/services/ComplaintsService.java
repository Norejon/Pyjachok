package com.example.pyjachok.services;

import com.example.pyjachok.dao.ComplaintsDAO;
import com.example.pyjachok.dao.EstablishmentDAO;
import com.example.pyjachok.dao.UserDAO;
import com.example.pyjachok.models.Complaints;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class ComplaintsService {

    private ComplaintsDAO complaintsDAO;
    private UserDAO userDAO;
    private EstablishmentDAO establishmentDAO;

    public void saveComplaint(Principal principal, int establishmentId, Complaints complaints) {
        String email = principal.getName();
        String username = userDAO.findByEmail(email).getUsername();
        String establishmentName = establishmentDAO.getById(establishmentId).getName();

        complaints.setUserUsername(username);
        complaints.setEstablishmentName(establishmentName);
        complaints.setText(complaints.getText());

        complaintsDAO.save(complaints);
        System.out.println("Complaint saved successfully");
    }

    public void deleteComplaint(int id) {
        complaintsDAO.deleteById(id);
    }

    public List<Complaints> getAllComplaints() {
        return complaintsDAO.findAll();
    }

    public Complaints getComplaintById(int id) {
        return complaintsDAO.findById(id);
    }

    public void updateComplaint(int id, Complaints updateComplaint) {
        Complaints existingComplaint = complaintsDAO.findById(id);
        if (existingComplaint != null) {
            existingComplaint.setText(updateComplaint.getText());
            complaintsDAO.save(existingComplaint);
            System.out.println("Complaint updated successfully");
        } else {
            System.out.println("Complaint not found");
        }
    }
}
