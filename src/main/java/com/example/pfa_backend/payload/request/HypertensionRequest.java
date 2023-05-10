package com.example.pfa_backend.payload.request;

import lombok.Data;

@Data
public class HypertensionRequest {
    private int age;
    private int sexe;
    private int cp;
    private int trestbps;
    private int chol;
    private int fbs;
    private int restecg;
    private int thalach;
    private int exang;
    private double oldpeak;
    private int slope;
    private int ca;
    private int thal;

}
