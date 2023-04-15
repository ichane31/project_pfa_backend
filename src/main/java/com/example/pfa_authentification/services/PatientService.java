package com.example.pfa_authentification.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.payload.request.PatientRequest;
import com.example.pfa_authentification.repositories.MedecinRepository;
import com.example.pfa_authentification.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    public Patient savePatient(Patient patient , Long id_medecin) throws NotFoundException {

        Medecin medecin = medecinRepository.findById(id_medecin).get();
        if(medecin ==null) {
            throw new NotFoundException(String.format("Medecin not found for id =%s" ,id_medecin));
        }
        patient.setMedecinTraitant(medecin);
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }

    public List<Patient> getPatientsByName(String name) {
        return patientRepository.findByNom(name);
    }

    public List<Patient> getPatientsByAge(int age) {
        return patientRepository.findByAge(age);
    }

    public List<Patient> getFirstTenPatientsOrderedByNameAsc(String nom) {
        return patientRepository.findAllByNomOrderByNomAsc(nom);
    }
    public List<Patient> getPatientsByNameAndAge(String name, int age) {
        return patientRepository.findPatientsByNomAndAge(name, age);
    }

    public List<Patient> getPatientsByMedecinId(Long medecinId) {
        Medecin medecin = medecinRepository.findById(medecinId).orElse(null);
        if (medecin == null) {
            return new ArrayList<>();
        }
        return medecin.getPatients();
    }

    public List<Patient> searchByNomAndPrenom(String nom, String prenom) {
        return patientRepository.findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(nom, prenom);
    }


    public List<Patient> searchByPrenom(String prenom) {
        return patientRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    public List<Patient> searchByNom(String nom) {
        return patientRepository.findByNomContainingIgnoreCase(nom);
    }

    public Patient updatePatient(Long id, PatientRequest patientRequest) throws NotFoundException, IOException {
        Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new NotFoundException("Patient not found with id: " + id));

        existingPatient.setAdresse(patientRequest.getAdresse());
        existingPatient.setAge(patientRequest.getAge());
        existingPatient.setDate_naissance(patientRequest.getDate_naissance());
        existingPatient.setNom(patientRequest.getNom());
        existingPatient.setPrenom(patientRequest.getPrenom());
        existingPatient.setEmail(patientRequest.getEmail());
        existingPatient.setSexe(patientRequest.getSexe());
        existingPatient.setTel(patientRequest.getTel());
        existingPatient.setGoup_sanguin(patientRequest.getGoup_sanguin());

        existingPatient.setImage(patientRequest.getImage().getBytes());


        return patientRepository.save(existingPatient);
    }
}
