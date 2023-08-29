package com.example.pyjachok.services;

import com.example.pyjachok.controllers.ExceptionController;
import com.example.pyjachok.dao.DrinkerDAO;
import com.example.pyjachok.models.Drinker;
import com.example.pyjachok.models.Establishment;
import com.example.pyjachok.models.User;
import com.example.pyjachok.models.dto.DrinkerDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@AllArgsConstructor
public class DrinkerService {
    private DrinkerDAO drinkerDAO;
    private UserService userService;
    private EstablishmentService establishmentService;
    private ExceptionController exceptionController;

    public void saveDrinker(int id,DrinkerDTO drinkerDTO, String email) {
        try {
            User user = userService.getUserByEmail(email);

            Establishment establishment = establishmentService.getById(id);
            if (establishment == null) {
                throw new IllegalArgumentException("Invalid establishment");
            }

            Drinker drinker = new Drinker();
            drinker.setDate(drinkerDTO.getDate());
            drinker.setTime(drinkerDTO.getTime());
            drinker.setDescription(drinkerDTO.getDescription());
            drinker.setCountOfPeople(drinkerDTO.getCountOfPeople());
            drinker.setWhoPay(drinkerDTO.getWhoPay());
            drinker.setBudget(drinkerDTO.getBudget());
            drinker.setPhone(drinkerDTO.getPhone());
            drinker.setUser(user);
            drinker.setEstablishment(establishment);

            drinkerDAO.save(drinker);
        } catch (Exception e) {
            exceptionController.valueException((MethodArgumentNotValidException) e);
        }
    }

    public void deleteDrinker(@PathVariable int id) {

        drinkerDAO.deleteById(id);
    }

    public List<Drinker> getAllDrinkers() {

        return drinkerDAO.findAll();
    }

    public Drinker getById(@PathVariable int id) {

        return drinkerDAO.findById(id);
    }

    public void updateDrinker(int id, Drinker updatedDrinker) {
        updatedDrinker.setId(id);
        drinkerDAO.save(updatedDrinker);
    }
}
