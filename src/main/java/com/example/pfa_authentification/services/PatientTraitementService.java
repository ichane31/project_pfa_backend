package com.example.pfa_authentification.services;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.PatientTraitement;
import com.example.pfa_authentification.models.Traitement;
import com.example.pfa_authentification.payload.request.PatientTraitementRequest;
import com.example.pfa_authentification.repositories.PatientTraitementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientTraitementService {

    @Autowired
    private PatientTraitementRepository patientTraitementRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private TraitementService traitementService;

    public PatientTraitement getById(Long id) throws NotFoundException {
        return patientTraitementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PatientTraitement not found with id : " + id));
    }

    public List<PatientTraitement> getAll() {
        return patientTraitementRepository.findAll();
    }

    public List<PatientTraitement> getByPatientId(Long patientId) {
        return patientTraitementRepository.findByPatient_Id(patientId);
    }

    public List<PatientTraitement> getByTraitementId(Long traitementId) {
        return patientTraitementRepository.findByTraitement_Id(traitementId);
    }

    public List<PatientTraitement> getByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return patientTraitementRepository.findByStartDateBetween(startDate, endDate);
    }

    public List<PatientTraitement> getByEndDateBetween(LocalDate startDate, LocalDate endDate) {
        return patientTraitementRepository.findByEndDateBetween(startDate, endDate);
    }

    public PatientTraitement create(PatientTraitementRequest patientTraitementRequest , Long id, Long id_trait) throws NotFoundException {
        Patient patient = patientService.getPatientById(id);
        Traitement traitement = traitementService.getById(id_trait);
        PatientTraitement patientTraitement = new PatientTraitement();
        patientTraitement.setPatient(patient);
        patientTraitement.setTraitement(traitement);
        patientTraitement.setStartDate(patientTraitementRequest.getDateDebut());
        patientTraitement.setEndDate(patientTraitementRequest.getDateFin());
        return patientTraitementRepository.save(patientTraitement);
    }

    public void deleteById(Long id) {
        patientTraitementRepository.deleteById(id);
    }

    public PatientTraitement update(Long id, PatientTraitementRequest patientTraitementRequest ) throws NotFoundException {
        PatientTraitement patientTraitement = patientTraitementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PatientTraitement not found with id : " + id));
        Patient patient = patientService.getPatientById(patientTraitement.getPatient().getId());
        Traitement traitement = traitementService.getById(patientTraitement.getTraitement().getId());
        patientTraitement.setPatient(patient);
        patientTraitement.setTraitement(traitement);
        patientTraitement.setStartDate(patientTraitementRequest.getDateDebut());
        patientTraitement.setEndDate(patientTraitementRequest.getDateFin());
        return patientTraitementRepository.save(patientTraitement);
    }

    public List<Traitement> getAllTraitementByPatientId(Long patientId) {
        List<PatientTraitement> patientTraitements = patientTraitementRepository.findByPatient_Id(patientId);
        return patientTraitements.stream().map(PatientTraitement::getTraitement).collect(Collectors.toList());
    }

    public List<Patient> getAllPatientByTraitementId(Long traitementId) {
        List<PatientTraitement> patientTraitements = patientTraitementRepository.findByTraitement_Id(traitementId);
        return patientTraitements.stream().map(PatientTraitement::getPatient).collect(Collectors.toList());
    }


}
