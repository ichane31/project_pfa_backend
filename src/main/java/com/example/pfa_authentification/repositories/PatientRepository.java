package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    @Query(value = "select * from patient where medecin =?1 ;",nativeQuery = true)
    List<Patient> findPatients();

    List<Patient> findByAge(int age);

    List<Patient> findByNom(String name);

    List<Patient> findAllByNomOrderByNomAsc(String nom);

    List<Patient> findPatientsByNomAndAge(String name, int age);

    //List<Patient> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndNumDossierContainingIgnoreCase(String nom, String prenom, String numDossier);

    //List<Patient> findByNomContainingIgnoreCaseAndDossiers_NumDossier(String nom, String numDossier);

    List<Patient> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(String nom, String prenom);

    List<Patient> findByPrenomContainingIgnoreCase(String prenom);

    List<Patient> findByNomContainingIgnoreCase(String nom);
}
