package com.example.pfa_authentification.payload.response;

import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.Traitement;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientTraitementResponse {
    private Traitement traitement;
    private Patient patient;
    private LocalDate startDate;
    private LocalDate endDate;

    public PatientTraitementResponse(Traitement traitement, LocalDate startDate, LocalDate endDate) {
        this.traitement = traitement;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PatientTraitementResponse(Patient patient, LocalDate startDate, LocalDate endDate) {
        this.patient = patient;
        this.startDate = startDate;
        this.endDate = endDate;
    }



}
