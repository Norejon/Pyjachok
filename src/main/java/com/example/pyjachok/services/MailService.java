package com.example.pyjachok.services;

import com.example.pyjachok.models.Complaints;
import com.example.pyjachok.models.dto.UserDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;

    public void send(UserDTO user) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(user.getEmail());
        helper.setText("<h3> http://localhost:8080/users/activation/"+user.getEmail()+ "</h3> "+"dsdad", true);
        javaMailSender.send(mimeMessage);
    }

    public void sendComplaint(Complaints complaint, Principal principal) throws MessagingException {
        String email = principal.getName();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo("mykolaskiy@gmail.com");
        helper.setText("<b> User with email: </b> <br>" + email + " <br> <b> Sent complaint with text: </b> <br>"+complaint.getText(), true);
        javaMailSender.send(mimeMessage);
    }
}
