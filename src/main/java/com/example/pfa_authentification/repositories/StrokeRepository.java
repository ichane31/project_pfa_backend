package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.Stroke;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

