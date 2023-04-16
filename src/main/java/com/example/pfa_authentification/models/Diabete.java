package com.example.pfa_authentification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Diabete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int age;
    private int sex;
    private int highChol;
    private int cholCheck;
    private double bmi;
    private int smoker;
    private int heartDiseaseorAttack;
    private int physActivity;
    private int fruits;
    private int veggies;
    private int hvyAlcoholConsump;
    private int genHlth;
    private int mentHlth;
    private int physHlth;
    private int diffWalk;
    private int stroke;
    private int highBP;
    private double resultat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Ignorer les propriétés du proxy Hibernate
    private Patient patient;

    public Diabete() {
    }

    public Diabete(Long id, int age, int sex, int highChol, int cholCheck, double bmi, int smoker, int heartDiseaseorAttack, int physActivity, int fruits, int veggies, int hvyAlcoholConsump, int genHlth, int mentHlth, int physHlth, int diffWalk, int stroke, int highBP, double resultat) {
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.highChol = highChol;
        this.cholCheck = cholCheck;
        this.bmi = bmi;
        this.smoker = smoker;
        this.heartDiseaseorAttack = heartDiseaseorAttack;
        this.physActivity = physActivity;
        this.fruits = fruits;
        this.veggies = veggies;
        this.hvyAlcoholConsump = hvyAlcoholConsump;
        this.genHlth = genHlth;
        this.mentHlth = mentHlth;
        this.physHlth = physHlth;
        this.diffWalk = diffWalk;
        this.stroke = stroke;
        this.highBP = highBP;
        this.resultat = resultat;
    }
}
