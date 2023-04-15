package com.example.pfa_authentification.payload.request;

import lombok.Data;


@Data
public class DiabeteRequest {

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

}
