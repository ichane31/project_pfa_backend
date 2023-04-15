package com.example.pfa_authentification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "horaire")
@Data
public class HoraireTravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DayOfWeek jourSemaine;

    private String heureDebut;

    private String heureFin;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    // Getters and setters
}
