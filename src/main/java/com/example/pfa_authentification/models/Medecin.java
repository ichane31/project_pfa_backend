package com.example.pfa_authentification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="medecin")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Medecin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "date_naissance")
    private LocalDate date_naissance;
    @Column(name = "age")
    private int age;
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "tel")
    private String tel;
    @Column(name = "email")
    private String email;
    @Column(name = "specialite")
    private String specialite;
    @Column
    private  String cin;
    @Column
    private LocalDate dateEmboche;
    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "medecinTraitant")
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<HoraireTravail> horairesTravail = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "medecin_secretaire",
            joinColumns = @JoinColumn(name = "medecin_id"),
            inverseJoinColumns = @JoinColumn(name = "secretaire_id"))
    private List<Secretaire> secretaires;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Traitement> traitements = new ArrayList<>();

}
