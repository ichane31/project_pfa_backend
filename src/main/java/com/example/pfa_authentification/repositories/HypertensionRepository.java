package com.example.pfa_authentification.repositories;

import com.example.pfa_authentification.models.Hypertension;
import com.example.pfa_authentification.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HypertensionRepository extends JpaRepository<Hypertension, Long> {
    List<Hypertension> findByTrestbpsGreaterThan(Integer trestbps);

    List<Hypertension> findByAgeGreaterThanEqualAndAgeLessThanEqual(Integer ageStart, Integer ageEnd);

    Long countByCp(Integer cp);

    List<Hypertension> findAllByPatient_Id(Long id);
}
