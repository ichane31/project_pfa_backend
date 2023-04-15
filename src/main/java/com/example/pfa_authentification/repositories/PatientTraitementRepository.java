package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.PatientTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientTraitementRepository extends JpaRepository<PatientTraitement, Long> {
    List<PatientTraitement> findByPatient_Id(Long patientId);
    List<PatientTraitement> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<PatientTraitement> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    List<PatientTraitement> findByTraitement_Id(Long id);
}
