package com.example.pfa_backend.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.Suivi;
import com.example.pfa_backend.services.SuiviService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/suivis")
public class SuiviController {

    @Autowired
    private SuiviService suiviService;


    @PostMapping("/patienttraitement/{id}")
    public ResponseEntity<?> createSuiviForPatientTraitement(@PathVariable(value = "id") Long patientTraitementId, 
                                                                  @RequestBody Suivi suivi) {
        try {
            Suivi newSuivi = suiviService.createSuivi(suivi ,patientTraitementId);
        return ResponseEntity.ok().body(newSuivi);
        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(e.getMessage());
        }
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSuiviById(@PathVariable(value = "id") Long id) {
        try {
            Suivi suivi =  suiviService.getSuiviById(id);
            return new ResponseEntity<>(suivi, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/")
    public List<Suivi> getAllSuivis() {
        return suiviService.getAllSuivis();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSuivi(@PathVariable(value = "id") Long id) {
        suiviService.deleteSuivi(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/suivis")
    public ResponseEntity<?> getAllSuiviForPatientTraitement(@PathVariable(value = "id") Long id) {
        try {
            List<Suivi> suivis = suiviService.getAllSuiviForPatientTraitement(id);
            return new ResponseEntity<>(suivis, HttpStatus.OK);

        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        }
    }
}

