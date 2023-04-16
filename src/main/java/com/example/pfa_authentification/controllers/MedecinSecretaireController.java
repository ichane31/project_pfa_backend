package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.InvalidException;
import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.MedecinSecretaire;
import com.example.pfa_authentification.models.Secretaire;
import com.example.pfa_authentification.payload.request.MedecinSecretaireRequest;
import com.example.pfa_authentification.services.MedecinSecretaireService;
import com.example.pfa_authentification.services.MedecinService;
import com.example.pfa_authentification.services.SecretaireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/medecinsSecretaire")
public class MedecinSecretaireController {

    private final MedecinService medecinService;
    private final SecretaireService secretaireService;
    private final MedecinSecretaireService medecinSecretaireService;

    public MedecinSecretaireController(MedecinService medecinService, SecretaireService secretaireService,
                                       MedecinSecretaireService medecinSecretaireService) {
        this.medecinService = medecinService;
        this.secretaireService = secretaireService;
        this.medecinSecretaireService = medecinSecretaireService;
    }

    @PostMapping("/{medecinId}/secretaires/{secretaireId}/relations")
    public ResponseEntity<?> addMedecinSecretaireRelation(@PathVariable Long medecinId,
                                                                                  @PathVariable Long secretaireId)  {
        MedecinSecretaire medecinSecretaire = null;
        try {
            medecinSecretaire = medecinSecretaireService.createMedecinSecretaire(medecinId, secretaireId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(medecinSecretaire, HttpStatus.OK);
    }

    @PutMapping("/{medecinId}/secretaires/{secretaireId}/relations")
    public ResponseEntity<?> updateMedecinSecretaireRelation(@PathVariable Long medecinId,
                                                                                     @PathVariable Long secretaireId,
                                                                                     @RequestBody MedecinSecretaireRequest medecinSecretaireRequest) {
        Medecin medecin = medecinService.getMedecinById(medecinId).get();
        Secretaire secretaire = secretaireService.getSecretaireById(secretaireId);
        MedecinSecretaire medecinSecretaire = null;
        try {
            medecinSecretaire = medecinSecretaireService.updateMedecinSecretaireRelation(medecin, secretaire, medecinSecretaireRequest);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(medecinSecretaire, HttpStatus.OK);
    }

    @DeleteMapping("/{medecinId}/secretaires/{secretaireId}/relations")
    public ResponseEntity<Void> deleteMedecinSecretaireRelation(@PathVariable Long medecinId, @PathVariable Long secretaireId)
            throws NotFoundException {
        medecinSecretaireService.deleteMedecinSecretaire(medecinId, secretaireId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint pour récupérer une relation active entre un médecin et une secrétaire.
     *
     * @param medecinId    l'identifiant du médecin
     * @param secretaireId l'identifiant de la secrétaire
     * @return la relation active entre le médecin et la secrétaire
     * @throws NotFoundException si le médecin ou la secrétaire n'existent pas, ou s'il n'y a pas de relation active entre eux
     */
    @GetMapping("/{medecinId}/secretaires/{secretaireId}")
    public ResponseEntity<?> getMedecinSecretaire(@PathVariable Long medecinId, @PathVariable Long secretaireId) throws NotFoundException {
        MedecinSecretaire medecinSecretaire= medecinSecretaireService.getMedecinSecretaire(medecinId, secretaireId);

        return ResponseEntity.ok().body(medecinSecretaire);

    }

    /**
     * Endpoint pour récupérer toutes les relations actives pour un médecin donné.
     *
     * @param medecinId l'identifiant du médecin
     * @return la liste des relations actives du médecin
     * @throws NotFoundException si le médecin n'existe pas
     */
    @GetMapping("/{medecinId}/secretaires")
    public ResponseEntity<List<MedecinSecretaire>> getMedecinSecretairesByMedecinId(@PathVariable Long medecinId) throws NotFoundException {
        List<MedecinSecretaire> secretaires= medecinSecretaireService.getMedecinSecretairesByMedecinId(medecinId);
        return ResponseEntity.ok().body(secretaires);
    }

    /**
     * Endpoint pour récupérer toutes les relations actives pour une secrétaire donnée.
     *
     * @param secretaireId l'identifiant de la secrétaire
     * @return la liste des relations actives de la secrétaire
     * @throws NotFoundException si la secrétaire n'existe pas
     */
    @GetMapping("/{secretaireId}/medecins")
    public ResponseEntity<List<MedecinSecretaire>> getMedecinSecretairesBySecretaireId(@PathVariable Long secretaireId) throws NotFoundException {
        List<MedecinSecretaire> secretaires = medecinSecretaireService.getMedecinSecretairesBySecretaireId(secretaireId);
        return ResponseEntity.ok().body(secretaires);

    }

    /**
     * Récupère toutes les secrétaires associées à un médecin donné.
     *
     * @param medecinId l'identifiant du médecin
     * @return la liste des secrétaires associées au médecin
     * @throws NotFoundException si le médecin n'existe pas
     */
    @GetMapping("/secretaires/{medecinId}")
    public ResponseEntity<List<Secretaire>> getSecretairesByMedecinId(@PathVariable Long medecinId) throws NotFoundException {
        List<Secretaire> secretaires = medecinSecretaireService.getSecretairesByMedecinId(medecinId);
        return ResponseEntity.ok().body(secretaires);
    }

    /**
     * Récupère le médecin associé à une secrétaire donnée.
     *
     * @param secretaireId l'identifiant de la secrétaire
     * @return le médecin associé à la secrétaire
     * @throws NotFoundException si la secrétaire n'existe pas
     */
    @GetMapping("/medecins/{secretaireId}")
    public ResponseEntity<?> getMedecinBySecretaireId(@PathVariable Long secretaireId) throws NotFoundException {
        List<Medecin> medecins = medecinSecretaireService.getMedecinsBySecretaireId(secretaireId);
        return ResponseEntity.ok().body(medecins);
    }
}
