package com.example.pfa_backend.payload.response;

import com.example.pfa_backend.models.Patient;
import com.example.pfa_backend.models.Traitement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientTraitementResponse {
    private Traitement traitement;
    private Patient patient;
    private String startDate;
    private String endDate;
    private String evaluation;

    public PatientTraitementResponse(Traitement traitement2, String startDate2, String endDate2) {
        this.traitement = traitement2;
        this.startDate = startDate2;
        this.endDate = endDate2;
    }

    public PatientTraitementResponse(Patient patient2, String startDate2, String endDate2) {
        this.patient = patient2;
        this.startDate = startDate2;
        this.endDate = endDate2;
    }

    public PatientTraitementResponse(Traitement traitement ,Patient patient2, String startDate2, String endDate2 ,String evaluation) {
        this.evaluation = evaluation;
        this.traitement = traitement;
        this.patient = patient2;
        this.startDate = startDate2;
        this.endDate = endDate2;
    }

}
