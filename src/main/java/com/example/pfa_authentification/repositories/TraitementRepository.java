package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.Traitement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TraitementRepository extends JpaRepository<Traitement, Long> {
    Traitement findTraitementByName(String name);

    List<Traitement> findTraitementByDescriptionContaining(String des);
}
