package com.example.pfa_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="patient")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "date_naissance")
    private String date_naissance;
    @Column(name = "email")
    private String email;
    @Column(name = "age")
    private int age;
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "tel")
    private String tel;

    @Column(name="group_sanguin")
    private String goup_sanguin;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecinTraitant;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stroke> strokes;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Diabete> diabetes;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Hypertension> hypertensions;

}
