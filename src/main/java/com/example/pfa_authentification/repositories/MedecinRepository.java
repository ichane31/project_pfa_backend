package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin,Long> {

    List<Medecin> findBySpecialite(String specialite);
    List<Medecin> findByNom(String nom);
    List<Medecin> findByAge(int age);
}
