package com.example.pfa_backend.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientTraitementRequest {

    private String dateDebut;

    private String dateFin;

    private String evaluationTraitement ;
}
