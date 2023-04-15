package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.payload.request.PatientRequest;
import com.example.pfa_authentification.repositories.PatientRepository;
import com.example.pfa_authentification.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping( path = "/add/medecin/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE} , produces = "application/json")
    public ResponseEntity savePatient(@ModelAttribute PatientRequest patient, @PathVariable("id") Long id,BindingResult result) {
        Patient patient1 = new Patient();
        patient1.setAdresse(patient.getAdresse());
        patient1.setAge(patient.getAge());
        patient1.setDate_naissance(patient.getDate_naissance());
        patient1.setNom(patient.getNom());
        patient1.setPrenom(patient.getPrenom());
        patient1.setEmail(patient.getEmail());
        patient1.setSexe(patient.getSexe());
        patient1.setTel(patient.getTel());
        patient1.setGoup_sanguin(patient.getGoup_sanguin());
        try {
            patient1.setImage(patient.getImage().getBytes());
        } catch (IOException e) {
            return new ResponseEntity("Image Error", HttpStatus.NO_CONTENT);
        }
        Patient savedPatient;
        try {
            savedPatient = patientService.savePatient(patient1, id);
        } catch (NotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(savedPatient, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePatientById(@PathVariable("id") Long id) {
        patientService.deletePatientById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Patient>> getPatientsByName(@PathVariable("name") String name) {
        List<Patient> patients = patientService.getPatientsByName(name);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<Patient>> getPatientsByAge(@PathVariable("age") int age) {
        List<Patient> patients = patientService.getPatientsByAge(age);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/first-ten-by-name")
    public ResponseEntity<List<Patient>> getFirstTenPatientsOrderedByNameAsc(String nom ) {
        List<Patient> patients = patientService.getFirstTenPatientsOrderedByNameAsc(nom);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/name/{name}/age/{age}")
    public ResponseEntity<List<Patient>> getPatientsByNameAndAge(@PathVariable("name") String name,
                                                                 @PathVariable("age") int age) {
        List<Patient> patients = patientService.getPatientsByNameAndAge(name, age);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE} , produces = "application/json")
    public ResponseEntity<?> updatePatient(@PathVariable("id") Long id,
                                                 @ModelAttribute PatientRequest patientRequest) {
        Patient updatedPatient = null;
        try {
            updatedPatient = patientService.updatePatient(id, patientRequest);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Image Error", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<Patient>> getPatientsByMedecinId(@PathVariable("medecinId") Long medecinId) {
        List<Patient> patients = patientService.getPatientsByMedecinId(medecinId);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            byte[] imageBytes = patient.getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new ByteArrayResource(imageBytes));        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}