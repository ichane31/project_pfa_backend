package com.example.pfa_authentification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer"})

public class Secretaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "date_naissance")
    private Date date_naissance;
    @Column(name = "age")
    private int age;
    @Column(name = "email")
    private String email;
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "tel")
    private String tel;
    @Column
    private  String cin;
    @Column
    private LocalDate dateEmboche;
    @Lob
    private byte[] image;

    @ManyToMany(mappedBy = "secretaires")
    @JsonIgnore
    private List<Medecin> medecins;

}
