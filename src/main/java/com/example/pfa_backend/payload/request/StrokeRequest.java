package com.example.pfa_backend.payload.request;

import lombok.Data;

@Data
public class StrokeRequest {

    private int age;
    private int sex;
    private int hypertension;
    private int heart_disease;
    private int ever_married;
    private int work_type;
    private int Residence_type;
    private double avg_glucose_level;
    private double bmi;
    private int smoking_status;
}
