package com.example.pfa_authentification.services;
import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Secretaire;
import com.example.pfa_authentification.payload.request.MedecinRequest;
import com.example.pfa_authentification.repositories.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private SecretaireService secretaireService;
    public List<Medecin> getAllMedecins() {
        return medecinRepository.findAll();
    }

    public Optional<Medecin> getMedecinById(Long id) {
        return medecinRepository.findById(id);
    }

    public List<Medecin> getMedecinsBySpecialite(String specialite) {
        return medecinRepository.findBySpecialite(specialite);
    }

    public List<Medecin> getMedecinsByNom(String nom) {
        return medecinRepository.findByNom(nom);
    }

    public List<Medecin> getMedecinsByAge(int age) {
        return medecinRepository.findByAge(age);
    }

    public Medecin saveMedecin(Medecin medecin ) {

        return medecinRepository.save(medecin);
    }

    public void deleteMedecin(Long id) {
        medecinRepository.deleteById(id);
    }

    public Medecin updateMedecin(Long id, MedecinRequest medecinRequest, List<Long> secretairesIds) throws NotFoundException {
        Optional<Medecin> medecinOptional = medecinRepository.findById(id);
        if (medecinOptional.isPresent()) {
            Medecin medecin = medecinOptional.get();
            medecin.setAdresse(medecinRequest.getAdresse());
            medecin.setAge(medecinRequest.getAge());
            medecin.setDate_naissance(medecinRequest.getDate_naissance());
            medecin.setNom(medecinRequest.getNom());
            medecin.setPrenom(medecinRequest.getPrenom());
            medecin.setEmail(medecinRequest.getEmail());
            medecin.setSexe(medecinRequest.getSexe());
            medecin.setTel(medecinRequest.getTel());
            medecin.setCin(medecinRequest.getCin());
            medecin.setSpecialite(medecinRequest.getSpecialite());
            medecin.setDateEmboche(medecinRequest.getDateEmboche());

            if (medecinRequest.getImage() != null) {
                try {
                    medecin.setImage(medecinRequest.getImage().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image");
                }
            }

            if (secretairesIds != null && !secretairesIds.isEmpty()) {
                List<Secretaire> secretaires = secretaireService.findAllById(secretairesIds);
                medecin.setSecretaires(secretaires);
            }

            return medecinRepository.save(medecin);
        } else {
            throw new NotFoundException("Medecin not found");
        }
    }

}

