package com.example.pfa_backend.services;

import com.example.pfa_backend.exception.AlreadyExistsException;
import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.*;
import com.example.pfa_backend.payload.request.MedecinSecretaireRequest;
import com.example.pfa_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedecinSecretaireService {

    @Autowired
    private MedecinSecretaireRepository medecinSecretaireRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private SecretaireRepository secretaireRepository;

    /**
     * Crée une relation active entre un médecin et une secrétaire.
     *
     * @param medecinId    l'identifiant du médecin
     * @param secretaireId l'identifiant de la secrétaire
     * @return la relation créée
     * @throws NotFoundException si le médecin ou la secrétaire n'existent pas
     *                           //@throws AlreadyExistsException si la relation existe déjà
     */
    public MedecinSecretaire createMedecinSecretaire(Long medecinId, Long secretaireId)
            throws NotFoundException, AlreadyExistsException {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new NotFoundException("Médecin non trouvé avec l'identifiant " + medecinId));
        Secretaire secretaire = secretaireRepository.findById(secretaireId)
                .orElseThrow(() -> new NotFoundException("Secrétaire non trouvé avec l'identifiant " + secretaireId));
        MedecinSecretaireId id = new MedecinSecretaireId(medecinId, secretaireId);
        MedecinSecretaire medecinSecretaire = medecinSecretaireRepository.findById(id).orElse(null);
        if (medecinSecretaire != null && medecinSecretaire.getEtatRelation() == EtatRelation.ACTIVE) {
            throw new AlreadyExistsException("La relation entre le médecin et la secrétaire existe déjà");
        }
        if (medecinSecretaire == null) {
            medecinSecretaire = new MedecinSecretaire();
            medecinSecretaire.setId(id);
        }
        medecinSecretaire.setMedecin(medecin);
        medecinSecretaire.setSecretaire(secretaire);
        medecinSecretaire.setEtatRelation(EtatRelation.ACTIVE);
        return medecinSecretaireRepository.save(medecinSecretaire);
    }

    /**
     * Supprime une relation active entre un médecin et une secrétaire.
     *
     * @param medecinId    l'identifiant du médecin
     * @param secretaireId l'identifiant de la secrétaire
     * @throws NotFoundException si le médecin ou la secrétaire n'existent pas, ou s'il n'y a pas de relation active entre eux
     */
    public void deleteMedecinSecretaire(Long medecinId, Long secretaireId) throws NotFoundException {
        MedecinSecretaireId id = new MedecinSecretaireId(medecinId, secretaireId);
        MedecinSecretaire medecinSecretaire = medecinSecretaireRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La relation entre le médecin et la secrétaire n'existe pas"));
        if (medecinSecretaire.getEtatRelation() != EtatRelation.ACTIVE) {
            throw new NotFoundException("La relation entre le médecin et la secrétaire n'est pas active");
        }
        medecinSecretaire.setEtatRelation(EtatRelation.INACTIVE);
        medecinSecretaireRepository.save(medecinSecretaire);
    }

    public MedecinSecretaire updateMedecinSecretaireRelation(Medecin medecin, Secretaire secretaire, MedecinSecretaireRequest medecinSecretaireRequest) throws NotFoundException {
        MedecinSecretaireId id = new MedecinSecretaireId(medecin.getId(), secretaire.getId());
        MedecinSecretaire medecinSecretaire = medecinSecretaireRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La relation entre le médecin et la secrétaire n'existe pas"));

        if (medecinSecretaire.getEtatRelation() != EtatRelation.ACTIVE) {
            throw new NotFoundException("La relation entre le médecin et la secrétaire n'est pas active");
        }

        medecinSecretaire.setDateFin(medecinSecretaireRequest.getDateFin());

        return medecinSecretaireRepository.save(medecinSecretaire);
    }

    /**
     * Récupère une relation active entre un médecin et une secrétaire.
     *
     * @param medecinId    l'identifiant du médecin
     * @param secretaireId l'identifiant de la secrétaire
     * @return la relation active entre le médecin et la secrétaire
     * @throws NotFoundException si le médecin ou la secrétaire n'existent pas, ou s'il n'y a pas de relation active entre eux
     */
    public MedecinSecretaire getMedecinSecretaire(Long medecinId, Long secretaireId) throws NotFoundException {
        MedecinSecretaireId id = new MedecinSecretaireId(medecinId, secretaireId);
        MedecinSecretaire medecinSecretaire = medecinSecretaireRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La relation entre le médecin et la secrétaire n'existe pas"));
        if (medecinSecretaire.getEtatRelation() != EtatRelation.ACTIVE) {
            throw new NotFoundException("La relation entre le médecin et la secrétaire n'est pas active");
        }
        return medecinSecretaire;
    }

    /**
     * Récupère toutes les relations actives pour un médecin donné.
     *
     * @param medecinId l'identifiant du médecin
     * @return la liste des relations actives du médecin
     * @throws NotFoundException si le médecin n'existe pas
     */
    public List<MedecinSecretaire> getMedecinSecretairesByMedecinId(Long medecinId) throws NotFoundException {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new NotFoundException("Médecin non trouvé avec l'identifiant " + medecinId));
        return medecinSecretaireRepository.findByMedecinAndEtatRelation(medecin, EtatRelation.ACTIVE);
    }

    /**
     * Récupère toutes les relations actives pour une secrétaire donnée.
     *
     * @param secretaireId l'identifiant de la secrétaire
     * @return la liste des relations actives de la secrétaire
     * @throws NotFoundException si la secrétaire n'existe pas
     */
    public List<MedecinSecretaire> getMedecinSecretairesBySecretaireId(Long secretaireId) throws NotFoundException {
        Secretaire secretaire = secretaireRepository.findById(secretaireId)
                .orElseThrow(() -> new NotFoundException("Secrétaire non trouvée avec l'identifiant " + secretaireId));
        return medecinSecretaireRepository.findBySecretaireAndEtatRelation(secretaire, EtatRelation.ACTIVE);
    }

    /**
     * Récupère toutes les secrétaires associées à un médecin donné.
     *
     * @param medecinId l'identifiant du médecin
     * @return la liste des secrétaires associées au médecin
     * @throws NotFoundException si le médecin n'existe pas
     */
    public List<Secretaire> getSecretairesByMedecinId(Long medecinId) throws NotFoundException {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new NotFoundException("Médecin non trouvé avec l'identifiant " + medecinId));
        List<MedecinSecretaire> medecinSecretaires = medecinSecretaireRepository.findByMedecinAndEtatRelation(medecin, EtatRelation.ACTIVE);
        List<Secretaire> secretaires = new ArrayList<>();
        for (MedecinSecretaire ms : medecinSecretaires) {
            secretaires.add(ms.getSecretaire());
        }
        return secretaires;
    }

    /**
     * Récupère les médecins associés à une secrétaire donnée.
     *
     * @param secretaireId l'identifiant de la secrétaire
     * @return la liste des médecins associés à la secrétaire
     * @throws NotFoundException si la secrétaire n'existe pas
     */
    public List<Medecin> getMedecinsBySecretaireId(Long secretaireId) throws NotFoundException {
        Secretaire secretaire = secretaireRepository.findById(secretaireId)
                .orElseThrow(() -> new NotFoundException("Secrétaire non trouvée avec l'identifiant " + secretaireId));
        List<MedecinSecretaire> medecinSecretaires = medecinSecretaireRepository.findBySecretaireAndEtatRelation(secretaire, EtatRelation.ACTIVE);
        List<Medecin> medecins = new ArrayList<>();
        for (MedecinSecretaire medecinSecretaire : medecinSecretaires) {
            medecins.add(medecinSecretaire.getMedecin());
        }
        return medecins;
    }


}