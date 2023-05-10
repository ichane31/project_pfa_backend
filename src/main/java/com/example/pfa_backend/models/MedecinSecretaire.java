package com.example.pfa_backend.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "medecin_secretaire")
@Data

public class MedecinSecretaire implements Serializable {

    @EmbeddedId
    private MedecinSecretaireId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medecinId")
    private Medecin medecin;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("secretaireId")
    private Secretaire secretaire;

    @Column(name = "date_attribution")
    private String dateAttribution;

    @Column(name = "date_fin")
    private String dateFin;

    @Column(name = "etat_relation")
    @Enumerated(EnumType.STRING)
    private EtatRelation etatRelation;


    // constructeurs, getters et setters
}
