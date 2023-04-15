package com.example.pfa_authentification.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedecinSecretaireRequest {
    private LocalDate dateAttribution;
    private LocalDate dateFin;
}
