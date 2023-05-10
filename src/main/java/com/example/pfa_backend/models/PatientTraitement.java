package com.example.pfa_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "patient_traitement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientTraitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "traitement_id")
    private Traitement traitement;

    @Column(name = "date_debut")
    private String startDate;

    @Column(name = "date_fin")
    private String endDate;

    @Column(name = "evaluation_traitement")
    private String evaluationTraitement;

    @OneToMany(mappedBy = "patientTraitement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Suivi> suivis;

    // constructors, getters and setters
}
