package com.example.pfa_authentification.payload.request;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RendezVousRequest {

    private LocalDate date;
    private String heureDebut;
    private String heureFin;
    private String motif;
}
