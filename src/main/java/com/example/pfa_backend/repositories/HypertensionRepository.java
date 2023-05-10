package com.example.pfa_backend.repositories;

import com.example.pfa_backend.models.Hypertension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HypertensionRepository extends JpaRepository<Hypertension, Long> {
    List<Hypertension> findByTrestbpsGreaterThan(Integer trestbps);

    List<Hypertension> findByAgeGreaterThanEqualAndAgeLessThanEqual(Integer ageStart, Integer ageEnd);

    Long countByCp(Integer cp);

    List<Hypertension> findAllByPatient_Id(Long id);

    Long countByPatient_MedecinTraitant_Id(Long id);

    @Query("SELECT COUNT(DISTINCT s.patient) FROM Stroke s")
    long countDistinctPatients();


}
