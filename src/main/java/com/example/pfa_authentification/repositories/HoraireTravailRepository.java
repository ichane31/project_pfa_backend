package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.HoraireTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;


@Repository
public interface HoraireTravailRepository extends JpaRepository<HoraireTravail, Long> {
    List<HoraireTravail> findByMedecin_IdAndJourSemaine(Long medecinId, DayOfWeek jourSemaine);
}
