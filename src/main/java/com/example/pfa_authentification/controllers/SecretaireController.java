package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.InvalidException;
import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Secretaire;
import com.example.pfa_authentification.payload.request.SecretaireRequest;
import com.example.pfa_authentification.services.SecretaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/secretaires")
public class SecretaireController {

    @Autowired
    SecretaireService secretaireService;

    @GetMapping
    public List<Secretaire> getAllSecretaires() {
        return secretaireService.getAllSecretaires();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Secretaire> getSecretaireById(@PathVariable("id") Long id) {
        Secretaire secretaire = secretaireService.getSecretaireById(id);
        if (secretaire != null) {
            return new ResponseEntity<>(secretaire, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity addSecretaire(@ModelAttribute SecretaireRequest secretaireRequest) {

        Secretaire newSecretaire = null;
        try {
            newSecretaire = secretaireService.addSecretaire(secretaireRequest);
        } catch (IOException e) {
            return new ResponseEntity<>("Image Error ", HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CREATED);
        } catch (InvalidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newSecretaire, HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{id}" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE} , produces = "application/json")
    public ResponseEntity<?> updateSecretaire(@PathVariable("id") Long id, @RequestBody SecretaireRequest secretaire) {
        Secretaire updatedSecretaire = null;
        try {
            updatedSecretaire = secretaireService.updateSecretaire(id, secretaire);
        } catch (IOException e) {
            return new ResponseEntity<>("Image Error ",HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        if (updatedSecretaire != null) {
            return new ResponseEntity<>(updatedSecretaire, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSecretaire(@PathVariable("id") Long id) {
        boolean deleted = secretaireService.deleteSecretaire(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Secretaire>> searchSecretaires(@RequestParam(value = "sexe", required = false) String sexe,
                                                              @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                              @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                              @RequestParam(value = "nom", required = false) String nom,
                                                              @RequestParam(value = "prenom", required = false) String prenom,
                                                              @RequestParam(value = "adresse", required = false) String adresse) {
        List<Secretaire> secretaires = secretaireService.searchSecretaires(sexe, startDate, endDate, nom, prenom, adresse);
        return new ResponseEntity<>(secretaires, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countSecretaires(@RequestParam(value = "sexe", required = false) String sexe,
                                                 @RequestParam(value = "adresse", required = false) String adresse) {
        Long count;
        if (sexe != null) {
            count = secretaireService.countBySexe(sexe);
        } else if (adresse != null) {
            count = secretaireService.countByAdresseContaining(adresse);
        } else {
            count = secretaireService.countAllSecretaires();
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
