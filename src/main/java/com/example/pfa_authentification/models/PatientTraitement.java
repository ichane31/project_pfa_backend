package com.example.pfa_authentification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "patient_traitement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientTraitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traitement_id")
    private Traitement traitement;

    @Column(name = "date_debut")
    private LocalDate startDate;

    @Column(name = "date_fin")
    private LocalDate endDate;

    // constructors, getters and setters
}
