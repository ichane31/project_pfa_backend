package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Secretaire;
import com.example.pfa_authentification.models.Stroke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SecretaireRepository extends JpaRepository<Secretaire, Long> {
    List<Secretaire> findBySexeAndDateEmbocheBetween(String sexe, LocalDate startDate, LocalDate endDate);
    List<Secretaire> findByNomAndPrenom(String nom, String prenom);
    @Query("SELECT COUNT(s) FROM Secretaire s WHERE s.sexe = :sexe")
    Long countBySexe(String sexe);
    List<Secretaire> findByAdresseContaining(String adresse);
    @Query("SELECT COUNT(s) FROM Secretaire s")
    Long countAllSecretaires();
}
