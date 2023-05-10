package com.example.pfa_backend.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Entity
@Data
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    private String description;

    @OneToMany( cascade = CascadeType.ALL)
    private List<Medecin> medecins;


    // getters et setters

}
