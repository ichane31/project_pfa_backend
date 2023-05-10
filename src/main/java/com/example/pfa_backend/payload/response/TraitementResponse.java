package com.example.pfa_backend.payload.response;

import com.example.pfa_backend.models.Traitement;

import lombok.Data;

@Data
public class TraitementResponse {

    private Traitement traitement;

    private int patientsCount ;

    public TraitementResponse(Traitement traitement2, int patientCount) {
        this.traitement =traitement2;
        this.patientsCount= patientCount;
    }
}
