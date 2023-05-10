package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.Medecin;
import com.example.pfa_backend.models.Patient;
import com.example.pfa_backend.models.Stroke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrokeRepository extends JpaRepository<Stroke, Long> {
    List<Stroke> findByResultat(double resultat);

    List<Stroke> findByHypertension(Boolean hypertension);

    List<Stroke> findByBmiLessThan(Double bmi);

    List<Stroke> findByAgeGreaterThanEqualAndAgeLessThanEqual(Integer ageStart, Integer ageEnd);

    Long countBySex(String sex);
    List<Stroke> findByPatient(Patient patient);

    List<Stroke> findByPatientId(Long patientId);

    List<Stroke> findByBmi(double bmi);

    List<Stroke> findBySex(int sexe);

    List<Stroke> findByResultat(Float resultat);

    long count();

    @Query("SELECT COUNT(DISTINCT s.patient) FROM Stroke s")
    long countDistinctPatients();

    long countByPatient_MedecinTraitant_Id(Long id);

}

