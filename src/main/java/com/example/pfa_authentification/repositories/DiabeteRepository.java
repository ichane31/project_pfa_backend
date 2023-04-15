package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Diabete;
import com.example.pfa_authentification.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiabeteRepository extends JpaRepository<Diabete, Long> {

    // Récupérer tous les diabètes avec un BMI supérieur à une certaine valeur
    List<Diabete> findByBmiGreaterThan(double bmi);

    // Récupérer tous les diabètes avec un certain état de santé général
    List<Diabete> findByGenHlth(int genHlth);

    // Récupérer tous les diabètes chez les patients qui ont déclaré ne pas boire d'alcool
    List<Diabete> findByHvyAlcoholConsump(int v);

    // Récupérer tous les diabètes chez les patients de sexe masculin âgés de plus de 50 ans
    List<Diabete> findBySexAndAgeGreaterThan(int sex, int age);

    // Compter le nombre de patients atteints de diabète ayant un physHlth inférieur à une certaine valeur
    long countByPhysHlthLessThan(int physHlth);

    List<Diabete> findByPatient(Patient patient);

    List<Diabete> findAllByPatient_Id(Long patientId);
}
