package com.example.pfa_authentification.services;

import com.example.pfa_authentification.exception.AlreadyExistsException;
import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.DossierPatient;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.payload.request.DossierPatientRequest;
import com.example.pfa_authentification.repositories.DossierPatientRepository;
import com.example.pfa_authentification.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DossierPatientService {

    @Autowired
    private DossierPatientRepository dossierPatientRepository;
    @Autowired
    PatientRepository patientRepository;

    public List<DossierPatient> getAllDossiers() {
        return dossierPatientRepository.findAll();
    }

    public Optional<DossierPatient> getDossierById(Long id) {
        return dossierPatientRepository.findById(id);
    }

    public DossierPatient updateDossier(DossierPatient dossier ,DossierPatientRequest dossierPatientRequest) {
        if(dossierPatientRequest!=null) {
            dossier.setNumeroDossier(dossierPatientRequest.getNumeroDossier());
            dossier.setAntecedents(dossierPatientRequest.getAntecedents());
        }

        return dossierPatientRepository.save(dossier);
    }

    public void deleteDossierById(Long id) {
        dossierPatientRepository.deleteById(id);
    }

    public DossierPatient getDossierByPatientId(Long patientId) {
        return dossierPatientRepository.findByPatient_Id(patientId);
    }

    public DossierPatient ajouterDossierPatient(Long idPatient, DossierPatientRequest dossierPatientRequest) {
        Optional<Patient> optionalPatient = patientRepository.findById(idPatient);
        DossierPatient dossierPatient= new DossierPatient();
        Optional<DossierPatient> existDossir = dossierPatientRepository.findByNumeroDossier(dossierPatientRequest.getNumeroDossier());
        if (optionalPatient.isPresent() && dossierPatientRequest!=null) {
            if (existDossir.isPresent()) {
                throw new AlreadyExistsException("Un dossier avec le même numéro existe déjà.");
            }
            Patient patient = optionalPatient.get();
            dossierPatient.setPatient(patient);
            dossierPatient.setNumeroDossier(dossierPatientRequest.getNumeroDossier());
            dossierPatient.setDateCreation(dossierPatientRequest.getDateCreation());
            dossierPatient.setAntecedents(dossierPatientRequest.getAntecedents());
            dossierPatientRepository.save(dossierPatient);
        } else {
            throw new RuntimeException("Patient non trouvé avec l'ID : " + idPatient);
        }
        return dossierPatient;
    }

    public List<DossierPatient> searchByNomAndPrenomAndNumDossier(String nom, String prenom, String numDossier) {
        return dossierPatientRepository.findByPatient_NomContainingIgnoreCaseAndPatient_PrenomContainingIgnoreCaseAndNumeroDossierContainingIgnoreCase(nom, prenom, numDossier);
    }

    public List<DossierPatient> searchByNomAndPrenom(String nom, String prenom) {
        return dossierPatientRepository.findByPatient_NomContainingIgnoreCaseAndPatient_PrenomContainingIgnoreCase(nom, prenom);
    }

    public List<DossierPatient> searchByNomAndNumDossier(String nom, String numDossier) {
        return dossierPatientRepository.findByPatient_NomContainingIgnoreCaseAndNumeroDossierContainingIgnoreCase(nom, numDossier);
    }

    public List<DossierPatient> searchByPrenomAndNumDossier(String prenom, String numDossier) {
        return dossierPatientRepository.findByPatient_PrenomContainingIgnoreCaseAndNumeroDossierContainingIgnoreCase(prenom, numDossier);
    }

    public List<DossierPatient> searchByNom(String nom) {
        return dossierPatientRepository.findByPatient_NomContainingIgnoreCase(nom);
    }

    public List<DossierPatient> searchByPrenom(String prenom) {
        return dossierPatientRepository.findByPatient_PrenomContainingIgnoreCase(prenom);
    }

    public List<DossierPatient> searchByNumDossier(String numDossier) {
        return dossierPatientRepository.findByNumeroDossierContainingIgnoreCase(numDossier);
    }

    public DossierPatient getDossierPatientByNumero(String numero) throws NotFoundException {
        Optional<DossierPatient> optionalDossierPatient = dossierPatientRepository.findByNumeroDossier(numero);
        if (optionalDossierPatient.isPresent()) {
            return optionalDossierPatient.get();
        } else {
            throw new NotFoundException("DossierPatient not found with numero : " + numero);
        }
    }

}
