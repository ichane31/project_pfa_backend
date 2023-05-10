package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    List<RendezVous> findByDateAndMedecin(String date, Medecin medecin);

    List<RendezVous> findByDate(String date);

    List<RendezVous> findByMedecin(Medecin medecin);

    List<RendezVous> findByPatientId(Long patientId);

    List<RendezVous> findByMedecinAndPatientAndDate(Medecin medecin, Patient patient, String date);
    List<RendezVous> findByMotifContainingIgnoreCase(String motif);

    List<RendezVous> findByMedecinId(Long medecinId);

    RendezVous findByMedecinAndDateAndHeureDebutAndHeureFin(Medecin medecin  , String date, String heudeDebut , String heureFin);



}
