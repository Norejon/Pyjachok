package com.example.pyjachok.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String website;
    private String phone;
    private String email;
    private String telegram;
    private String instagram;
    private String others;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "establishment_id")
    @JsonBackReference
    private Establishment establishment;
}
