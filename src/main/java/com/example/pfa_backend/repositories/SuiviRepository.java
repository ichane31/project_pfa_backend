package com.example.pfa_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pfa_backend.models.PatientTraitement;
import com.example.pfa_backend.models.Suivi;

public interface SuiviRepository extends JpaRepository<Suivi, Long> {

    List<Suivi> findByPatientTraitement(PatientTraitement patientTraitement);

}
