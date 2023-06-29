package com.example.pyjachok.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "drinkers")
@JsonIgnoreProperties(value = {"user"}, allowSetters = true)
public class Drinker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String date;
    private String time;
    private String description;
    private int countOfPeople;
    private String whoPay;
    private int budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id")
    @JsonIgnore
    private Establishment establishment;
}
