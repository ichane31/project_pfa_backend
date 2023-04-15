package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    List<RendezVous> findByDateAndMedecin(LocalDate date, Medecin medecin);

    List<RendezVous> findByDate(LocalDate date);

    List<RendezVous> findByMedecin(Medecin medecin);

    List<RendezVous> findByPatientId(Long patientId);

    List<RendezVous> findByMedecinAndPatientAndDate(Medecin medecin, Patient patient, LocalDate date);
    List<RendezVous> findByMotifContainingIgnoreCase(String motif);

    List<RendezVous> findByMedecinId(Long medecinId);
}
