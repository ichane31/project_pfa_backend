package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedecinSecretaireRepository extends JpaRepository<MedecinSecretaire, MedecinSecretaireId> {
    List<MedecinSecretaire> findByMedecinAndEtatRelation(Medecin medecin, EtatRelation active);

    List<MedecinSecretaire> findBySecretaireAndEtatRelation(Secretaire secretaire, EtatRelation active);

    // méthodes de requête spécifiques à ajouter ici si nécessaire

}
