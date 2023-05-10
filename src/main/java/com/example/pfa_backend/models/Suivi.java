package com.example.pfa_backend.models;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "suivi_traitement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suivi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_traitement_id")
    private PatientTraitement patientTraitement;

    @Column(name = "date_suivi")
    private String dateSuivi;

    @Column(name = "commentaire")
    private String commentaire;

    // constructors, getters and setters
}
