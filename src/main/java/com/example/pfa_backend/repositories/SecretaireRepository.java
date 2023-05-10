package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.Secretaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SecretaireRepository extends JpaRepository<Secretaire, Long> {
    List<Secretaire> findBySexeAndDateEmbocheBetween(String sexe, String startDate, String endDate);
    List<Secretaire> findByNomAndPrenom(String nom, String prenom);
    @Query("SELECT COUNT(s) FROM Secretaire s WHERE s.sexe = :sexe")
    Long countBySexe(String sexe);
    List<Secretaire> findByAdresseContaining(String adresse);
    @Query("SELECT COUNT(s) FROM Secretaire s")
    Long countAllSecretaires();

    Secretaire findByEmail(String email);
}
