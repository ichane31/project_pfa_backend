package com.example.pfa_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.*;
import com.example.pfa_backend.repositories.*;

@Service
public class SuiviService {
    @Autowired
    private SuiviRepository suiviRepository;

    @Autowired
    private PatientTraitementRepository patientTraitementRepository;

    @Autowired
    PatientTraitementService patientTraitementService;
    
    public Suivi createSuivi(Suivi suivi) {
        return suiviRepository.save(suivi);
    }

    public Suivi createSuivi(Suivi suivi, Long patientTraitementId) throws NotFoundException {
        PatientTraitement patientTraitement = patientTraitementService.getById(patientTraitementId);
        suivi.setPatientTraitement(patientTraitement);
        return suiviRepository.save(suivi);
    }
    
    

    public Suivi getSuiviById(Long id) throws NotFoundException {
        return suiviRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Suivi not Found"));
    }

    public List<Suivi> getAllSuivis() {
        return suiviRepository.findAll();
    }

    public void deleteSuivi(Long id) {
        suiviRepository.deleteById(id);
    }

    public List<Suivi> getAllSuiviForPatientTraitement(Long patientTraitementId) throws NotFoundException {
        PatientTraitement patientTraitement = patientTraitementRepository.findById(patientTraitementId)
                .orElseThrow(() -> new NotFoundException("PatientTraitement"));
        return suiviRepository.findByPatientTraitement(patientTraitement);
    }
}
