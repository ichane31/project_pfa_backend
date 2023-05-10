package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.AlreadyExistsException;
import com.example.pfa_backend.models.DossierPatient;
import com.example.pfa_backend.payload.request.DossierPatientRequest;
import com.example.pfa_backend.services.DossierPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/dossiers")
public class DossierPatientController {

    @Autowired
    private DossierPatientService dossierPatientService;

    @GetMapping("/")
    public List<DossierPatient> getAllDossiers() {
        return dossierPatientService.getAllDossiers();
    }

    @PostMapping("/{idPatient}/add")
    public ResponseEntity<?> ajouterDossierPatient(@PathVariable Long idPatient, @RequestBody DossierPatientRequest dossierPatientRequest) {
        try {
            DossierPatient dossierPatient = dossierPatientService.ajouterDossierPatient(idPatient, dossierPatientRequest);
            return new ResponseEntity<>(dossierPatient, HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DossierPatient> getDossierById(@PathVariable Long id) {
        return dossierPatientService.getDossierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DossierPatient> updateDossier(@PathVariable Long id, @RequestBody DossierPatientRequest dossierPatientRequest) {
        DossierPatient dossierPatient= dossierPatientService.getDossierById(id).get();
        if (!dossierPatientService.getDossierById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dossierPatient.setId(id);
        return ResponseEntity.ok().body(dossierPatientService.updateDossier(dossierPatient,dossierPatientRequest));
    }

    @GetMapping("/{id}/byPatient")
    public ResponseEntity<?> getDossierPatient(@PathVariable Long id) {
        DossierPatient dossierPatient= dossierPatientService.getDossierByPatientId(id);
        if (dossierPatient==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(dossierPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DossierPatient> deleteDossierById(@PathVariable Long id) {
        if (!dossierPatientService.getDossierById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dossierPatientService.deleteDossierById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/dossiers/search")
    public List<DossierPatient> searchDossiers(
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "prenom", required = false) String prenom,
            @RequestParam(value = "numDossier", required = false) String numDossier) {
        if (nom != null && prenom != null && numDossier != null) {
            return dossierPatientService.searchByNomAndPrenomAndNumDossier(nom, prenom, numDossier);
        } else if (nom != null && prenom != null) {
            return dossierPatientService.searchByNomAndPrenom(nom, prenom);
        } else if (nom != null && numDossier != null) {
            return dossierPatientService.searchByNomAndNumDossier(nom, numDossier);
        } else if (prenom != null && numDossier != null) {
            return dossierPatientService.searchByPrenomAndNumDossier(prenom, numDossier);
        } else if (nom != null) {
            return dossierPatientService.searchByNom(nom);
        } else if (prenom != null) {
            return dossierPatientService.searchByPrenom(prenom);
        } else if (numDossier != null) {
            return dossierPatientService.searchByNumDossier(numDossier);
        } else {
            return dossierPatientService.getAllDossiers();
        }
    }
}
