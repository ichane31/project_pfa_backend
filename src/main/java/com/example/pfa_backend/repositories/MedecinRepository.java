package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin,Long> {

    List<Medecin> findBySpecialite(String specialite);
    List<Medecin> findByNom(String nom);
    List<Medecin> findByAge(int age);

    Medecin findByEmail(String email);
}
