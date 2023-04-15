package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.payload.request.MedecinRequest;
import com.example.pfa_authentification.services.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/medecins")
public class MedecinController {

    @Autowired
    private MedecinService medecinService;

    @GetMapping
    public ResponseEntity<List<Medecin>> getAllMedecins() {
        List<Medecin> medecins = medecinService.getAllMedecins();
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medecin> getMedecinById(@PathVariable Long id) {
        Optional<Medecin> medecin = medecinService.getMedecinById(id);
        return medecin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<Medecin>> getMedecinsBySpecialite(@PathVariable String specialite) {
        List<Medecin> medecins = medecinService.getMedecinsBySpecialite(specialite);
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Medecin>> getMedecinsByNom(@PathVariable String nom) {
        List<Medecin> medecins = medecinService.getMedecinsByNom(nom);
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<Medecin>> getMedecinsByAge(@PathVariable int age) {
        List<Medecin> medecins = medecinService.getMedecinsByAge(age);
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} , produces = "application/json")
    public ResponseEntity<Medecin> saveMedecin(@ModelAttribute MedecinRequest medecinRequest) {
        Medecin medecin = new Medecin();
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

        try {
            medecin.setImage(medecinRequest.getImage().getBytes());
        } catch (IOException e) {
            return new ResponseEntity("Image Error", HttpStatus.NO_CONTENT);
        }
        Medecin savedMedecin = medecinService.saveMedecin(medecin );
        return new ResponseEntity<>(savedMedecin, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Long id) {
        medecinService.deleteMedecin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity<?> updateMedecin(@PathVariable Long id, @ModelAttribute MedecinRequest medecinRequest,
                                                 @RequestParam(value = "secretairesIds", required = false) List<Long> secretairesIds) {
        Medecin medecin = null;
        try {
            medecin = medecinService.updateMedecin(id, medecinRequest, secretairesIds);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(medecin, HttpStatus.OK);
    }
}
