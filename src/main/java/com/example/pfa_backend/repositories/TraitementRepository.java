package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.Medecin;
import com.example.pfa_backend.models.Traitement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TraitementRepository extends JpaRepository<Traitement, Long> {
    Traitement findTraitementByName(String name);

    List<Traitement> findTraitementByDescriptionContaining(String des);

    List<Traitement> findByMedecin(Medecin medecin);
}
