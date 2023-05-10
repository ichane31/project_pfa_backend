package com.example.pfa_backend.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedecinSecretaireRequest {
    private String dateAttribution;
    private String dateFin;
}
