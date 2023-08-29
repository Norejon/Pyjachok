package com.example.pyjachok.services;

import com.example.pyjachok.dao.ContactDAO;
import com.example.pyjachok.models.Contacts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactsService {

    private ContactDAO contactDAO;

    public void saveContacts(Contacts contacts){
    contactDAO.save(contacts);
    }
}
