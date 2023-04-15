package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.Secretaire;
import com.example.pfa_authentification.repositories.MedecinRepository;
import com.example.pfa_authentification.repositories.PatientRepository;
import com.example.pfa_authentification.repositories.SecretaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    MedecinRepository medecinRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    SecretaireRepository secretaireRepository;

    @GetMapping("/{type}/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable String type, @PathVariable Long id) {
        byte[] imageBytes = null;
        String contentType = null;

        switch (type) {
            case "patient":
                Optional<Patient> patientOptional = patientRepository.findById(id);
                if (patientOptional.isPresent()) {
                    Patient patient = patientOptional.get();
                    imageBytes = patient.getImage();
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                }
                break;

            case "medecin":
                Optional<Medecin> medecinOptional = medecinRepository.findById(id);
                if (medecinOptional.isPresent()) {
                    Medecin medecin = medecinOptional.get();
                    imageBytes = medecin.getImage();
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                }
                break;

            case "secretaire":
                Optional<Secretaire> secretaireOptional = secretaireRepository.findById(id);
                if (secretaireOptional.isPresent()) {
                    Secretaire secretaire = secretaireOptional.get();
                    imageBytes = secretaire.getImage();
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                }
                break;

            default:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (imageBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(contentType));
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(contentType))
                    .body(new ByteArrayResource(imageBytes));
        } else {
            return new ResponseEntity<>("Image not Found for this " + type + " with ID " +id,HttpStatus.NOT_FOUND);
        }
    }

}
