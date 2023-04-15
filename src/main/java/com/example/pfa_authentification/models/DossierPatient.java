package com.example.pfa_authentification.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class DossierPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroDossier;

    private LocalDate dateCreation;

    @Column(columnDefinition="TEXT")
    private String antecedents;

    @OneToOne(fetch = FetchType.LAZY)
    private Patient patient;

    // constructeurs, getters et setters
}
