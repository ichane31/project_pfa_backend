package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.PatientTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientTraitementRepository extends JpaRepository<PatientTraitement, Long> {
    List<PatientTraitement> findByPatient_Id(Long patientId);
    List<PatientTraitement> findByStartDateBetween(String startDate, String endDate);
    List<PatientTraitement> findByEndDateBetween(String startDate, String endDate);

    List<PatientTraitement> findByTraitement_Id(Long id);
}
