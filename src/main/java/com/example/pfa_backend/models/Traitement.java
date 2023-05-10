package com.example.pfa_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer"})
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

    public int getPatientCount() {
        return patientTreatments.size();
    }
    // constructors, getters, setters, and other methods
}
