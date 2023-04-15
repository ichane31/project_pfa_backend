package com.example.pfa_authentification.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientTraitementRequest {

    private LocalDate dateDebut;

    private LocalDate dateFin;
}
