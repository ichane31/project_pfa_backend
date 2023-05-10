package com.example.pfa_backend.controllers;

import com.example.pfa_backend.models.*;
import com.example.pfa_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    
    @PutMapping("/{type}/{id}/image")
    public ResponseEntity<?> uploadImage(@PathVariable String type, @PathVariable Long id, @RequestPart("image") MultipartFile file) {

        switch (type) {
            case "patient":
                Patient patient = patientRepository.getById(id);
                if (patient !=null) {
                    try {
                        patient.setImage(file.getBytes());
                        patientRepository.save(patient);
                        return new ResponseEntity<>("Image has been uploaded successfully.", HttpStatus.OK);
                    } catch (IOException e) {
                        return new ResponseEntity<>("Image exeption.", HttpStatus.NO_CONTENT);
                    }
                }
                else {
                    return new ResponseEntity<>("User not Found for this " + type + " with ID " + id, HttpStatus.NOT_FOUND);
                }
            case "medecin":
                Medecin medecin = medecinRepository.getById(id);
                if (medecin !=null) {
                    try {
                        medecin.setImage(file.getBytes());
                        medecinRepository.save(medecin);
                        return new ResponseEntity<>("Image has been uploaded successfully.", HttpStatus.OK);
                    } catch (IOException e) {
                        return new ResponseEntity<>("Image exeption.", HttpStatus.NO_CONTENT);
                    }
                }
                else {
                    return new ResponseEntity<>("User not Found for this " + type + " with ID " + id, HttpStatus.NOT_FOUND);
                }

            case "secretaire":
                Secretaire secretaire = secretaireRepository.getById(id);
                if (secretaire !=null) {
                    try {
                        secretaire.setImage(file.getBytes());
                        secretaireRepository.save(secretaire);
                        return new ResponseEntity<>("Image has been uploaded successfully.", HttpStatus.OK);
                    } catch (IOException e) {
                        return new ResponseEntity<>("Image exeption.", HttpStatus.NO_CONTENT);
                    }
                }
                else {
                    return new ResponseEntity<>("User not Found for this " + type + " with ID " + id, HttpStatus.NOT_FOUND);
                }
            default:
            return new ResponseEntity<>("User not Found for this " + type , HttpStatus.NOT_FOUND);

        }
      
    }

}
