package com.example.pfa_authentification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
public class Traitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;


    @OneToMany(mappedBy = "traitement", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PatientTraitement> patientTreatments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    // constructors, getters, setters, and other methods
}
