package com.example.pfa_backend.email.context;

import com.example.pfa_backend.models.RendezVous;

public class RendezVousPatientEmailContext extends AbstractEmailContext{
    @Override
    public <T> void init(T context) {
        //we can do any common configuration setup here
        // like setting up some base URL and context
        RendezVous rendezVous = (RendezVous) context; // we pass the customer informati
        put("Nom_Prenom", rendezVous.getPatient().getNom() + " " +rendezVous.getPatient().getPrenom());
        put("Date",rendezVous.getDate());
        put("Heure","De " + rendezVous.getHeureDebut() + " à " + rendezVous.getHeureFin());
        setTemplateLocation("rendez_vous");
        setSubject("Informations Du RendezVous");
        setFrom("projectensaj2023@gmail.com");
        setTo(rendezVous.getPatient().getEmail());
    }

    @Override
    public <T> void init(T context, String password) {

    }
}
