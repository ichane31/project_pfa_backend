package com.example.pfa_backend.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DossierPatientRequest {
    private String numeroDossier;

    private LocalDate dateCreation;

    private String antecedents;
}
