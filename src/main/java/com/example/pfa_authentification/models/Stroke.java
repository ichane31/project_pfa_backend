package com.example.pfa_authentification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Stroke{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;
    private int sex;
    private double resultat;

    private int hypertension;
    private int heart_disease;
    private int ever_married;
    private int work_type;
    private int Residence_type;
    private double avg_glucose_level;
    private double bmi;
    private int smoking_status;

    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;


    public Stroke() {
        super();
    }

    public Stroke(Long id, int age, int sex, double resultat, int hypertension, int heart_disease, int ever_married, int work_type, int residence_type, double avg_glucose_level, double bmi, int smoking_status) {
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.resultat = resultat;
        this.hypertension = hypertension;
        this.heart_disease = heart_disease;
        this.ever_married = ever_married;
        this.work_type = work_type;
        this.Residence_type = residence_type;
        this.avg_glucose_level = avg_glucose_level;
        this.bmi = bmi;
        this.smoking_status = smoking_status;
    }
}

