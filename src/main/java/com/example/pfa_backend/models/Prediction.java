package com.example.pfa_backend.models;

public class Prediction {

    private int age;
    private String sexe;
    private Float resultat;

    public Prediction() {
    }

    public Prediction(int age, String sexe, Float resultat) {
        this.age = age;
        this.sexe = sexe;
        this.resultat = resultat;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Float getResultat() {
        return resultat;
    }

    public void setResultat(Float resultat) {
        this.resultat = resultat;
    }
}
