package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.DossierPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierPatientRepository extends JpaRepository<DossierPatient, Long> {
    DossierPatient findByPatient_Id(Long id);

    List<DossierPatient> findByPatient_NomContainingIgnoreCaseAndNumeroDossierContainingIgnoreCase(String nom, String numDossier);

    List<DossierPatient> findByPatient_PrenomContainingIgnoreCaseAndNumeroDossierContainingIgnoreCase(String prenom, String numDossier);

    List<DossierPatient> findByPatient_NomContainingIgnoreCase(String nom);

    List<DossierPatient> findByPatient_PrenomContainingIgnoreCase(String prenom);

    List<DossierPatient> findByNumeroDossierContainingIgnoreCase(String numDossier);

    List<DossierPatient> findByPatient_NomContainingIgnoreCaseAndPatient_PrenomContainingIgnoreCase(String nom, String prenom);

    List<DossierPatient> findByPatient_NomContainingIgnoreCaseAndPatient_PrenomContainingIgnoreCaseAndNumeroDossierContainingIgnoreCase(String nom, String prenom, String numDossier);

    Optional<DossierPatient> findByNumeroDossier(String numero);
}
