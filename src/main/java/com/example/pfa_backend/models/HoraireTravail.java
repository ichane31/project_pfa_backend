package com.example.pfa_backend.models;

import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;

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
