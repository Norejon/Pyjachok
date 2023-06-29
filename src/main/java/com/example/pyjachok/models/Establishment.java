package com.example.pyjachok.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "establishments")
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;
    private String photo;
    private String type;
    private String tags;
    private int rating;
    private int midle_check;
    private String registration_date;
    private String location;
    private String schedule;
    private boolean activated = false;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "establishment")
    @JsonManagedReference
    private List<Grades> gradesList;

    @OneToMany(mappedBy = "establishment")
    private List<News> news;

    @OneToMany(mappedBy = "establishment")
    private List<Drinker> drinkers;
}
