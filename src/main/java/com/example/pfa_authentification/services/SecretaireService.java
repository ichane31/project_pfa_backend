package com.example.pfa_authentification.services;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Secretaire;
import com.example.pfa_authentification.payload.request.SecretaireRequest;
import com.example.pfa_authentification.repositories.MedecinRepository;
import com.example.pfa_authentification.repositories.SecretaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SecretaireService {

    @Autowired
    SecretaireRepository secretaireRepository;

    @Autowired
    MedecinRepository medecinRepository;

    public List<Secretaire> getAllSecretaires() {
        return secretaireRepository.findAll();
    }

    public Secretaire getSecretaireById(Long id) {
        return secretaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Secretaire introuvable"));
    }

    public Secretaire addSecretaire(SecretaireRequest secretaireRequest) throws IOException, NotFoundException {
        Secretaire secretaire = new Secretaire();
        secretaire.setNom(secretaireRequest.getNom());
        secretaire.setPrenom(secretaireRequest.getPrenom());
        secretaire.setDate_naissance(secretaireRequest.getDate_naissance());
        secretaire.setAge(secretaireRequest.getAge());
        secretaire.setSexe(secretaireRequest.getSexe());
        secretaire.setAdresse(secretaireRequest.getAdresse());
        secretaire.setTel(secretaireRequest.getTel());
        secretaire.setCin(secretaireRequest.getCin());
        secretaire.setDateEmboche(secretaireRequest.getDateEmboche());
        try {
            secretaire.setImage(secretaireRequest.getImage().getBytes());
        } catch (IOException e) {
            throw new IOException("Error while reading image data");
        }

        return secretaireRepository.save(secretaire);
    }


    @Autowired
    public SecretaireService(SecretaireRepository secretaireRepository) {
        this.secretaireRepository = secretaireRepository;
    }

    public List<Secretaire> findBySexeAndDateEmbocheBetween(String sexe, LocalDate startDate, LocalDate endDate) {
        return secretaireRepository.findBySexeAndDateEmbocheBetween(sexe, startDate, endDate);
    }

    public List<Secretaire> findByNomAndPrenom(String nom, String prenom) {
        return secretaireRepository.findByNomAndPrenom(nom, prenom);
    }

    public Long countBySexe(String sexe) {
        return secretaireRepository.countBySexe(sexe);
    }

    public List<Secretaire> findByAdresseContaining(String adresse) {
        return secretaireRepository.findByAdresseContaining(adresse);
    }

    public Long countAllSecretaires() {
        return secretaireRepository.countAllSecretaires();
    }


    public Secretaire updateSecretaire(Long id, SecretaireRequest secretaire) throws IOException, NotFoundException {
        Secretaire existingSecretaire = secretaireRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Secretaire with id " + id + " not found"));

        existingSecretaire.setNom(secretaire.getNom());
        existingSecretaire.setPrenom(secretaire.getPrenom());
        existingSecretaire.setDate_naissance(secretaire.getDate_naissance());
        existingSecretaire.setAge(secretaire.getAge());
        existingSecretaire.setSexe(secretaire.getSexe());
        existingSecretaire.setAdresse(secretaire.getAdresse());
        existingSecretaire.setTel(secretaire.getTel());
        existingSecretaire.setCin(secretaire.getCin());
        existingSecretaire.setDateEmboche(secretaire.getDateEmboche());
        existingSecretaire.setImage(secretaire.getImage().getBytes());

        return secretaireRepository.save(existingSecretaire);
    }

    public boolean deleteSecretaire(Long id) {
        Secretaire secretaire = secretaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Secretaire with id " + id + " not found"));

        secretaireRepository.delete(secretaire);
        return false;
    }

    public List<Secretaire> searchSecretaires(String sexe, LocalDate startDate, LocalDate endDate, String nom, String prenom, String adresse) {
        List<Secretaire> result = new ArrayList<>();

        // Chercher par sexe et date d'embauche
        if (sexe != null && startDate != null && endDate != null) {
            result.addAll(secretaireRepository.findBySexeAndDateEmbocheBetween(sexe, startDate, endDate));
        }

        // Chercher par nom et prénom
        if (nom != null && prenom != null) {
            result.addAll(secretaireRepository.findByNomAndPrenom(nom, prenom));
        }

        // Chercher par adresse
        if (adresse != null) {
            result.addAll(secretaireRepository.findByAdresseContaining(adresse));
        }

        return result;
    }

    public Long countByAdresseContaining(String adresse) {
        List<Secretaire> secretaires = secretaireRepository.findByAdresseContaining(adresse);
        return (long) secretaires.size();
    }

    public List<Secretaire> findByMedecinId(Long medecinId) {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new EntityNotFoundException("Medecin not found with id " + medecinId));
        return medecin.getSecretaires();
    }


    public List<Secretaire> findAllById(List<Long> ids) {
        return secretaireRepository.findAllById(ids);
    }


}
