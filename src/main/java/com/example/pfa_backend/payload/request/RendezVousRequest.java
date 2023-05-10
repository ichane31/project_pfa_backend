package com.example.pfa_backend.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RendezVousRequest {

    private String date;
    private String heureDebut;
    private String heureFin;
    private String motif;
}
