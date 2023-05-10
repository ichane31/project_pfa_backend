package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.InvalidException;
import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.ERole;
import com.example.pfa_backend.models.Patient;
import com.example.pfa_backend.models.Role;
import com.example.pfa_backend.models.User;
import com.example.pfa_backend.payload.request.PatientRequest;
import com.example.pfa_backend.repositories.PatientRepository;
import com.example.pfa_backend.repositories.RoleRepository;
import com.example.pfa_backend.repositories.UserRepository;
import com.example.pfa_backend.services.PatientService;
import com.example.pfa_backend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
        } catch (InvalidException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(savedPatient, HttpStatus.OK);
    }

    @PostMapping( path = "/addWithAccount/medecin/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE} , produces = "application/json")
    public ResponseEntity savePatientWithAccount(@ModelAttribute PatientRequest patient, @PathVariable("id") Long id,BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        // Créer un nouvel utilisateur pour le patient
        User user = new User();
        user.setFirstName(patient.getPrenom());
        user.setLastName(patient.getNom());
        user.setEmail(patient.getEmail());
        user.setPassword(passwordEncoder.encode(patient.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole;
        try {
            userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                        .orElseThrow(() -> new NotFoundException("error: role is not found."));
            roles.add(userRole);

        } catch (NotFoundException e) {
            
        }
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        userService.sendRegistrationConfirmationEmail22(user, patient.getPassword());

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
            if(patient.getImage() != null) {
                patient1.setImage(patient.getImage().getBytes());
            }
        } catch (IOException e) {
            return new ResponseEntity("Image Error", HttpStatus.NO_CONTENT);
        }
        Patient savedPatient;
        try {
            savedPatient = patientService.savePatient(patient1, id);
        } catch (NotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
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
/* 
    @PutMapping(path = "/update/{id}" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.ALL_VALUE} , produces = "application/json")
    public ResponseEntity<?> updatePatient(@PathVariable("id") Long id,
                                                 @RequestParam PatientRequest patientRequest) {
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
*/
    @PutMapping(path = "/update/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_OCTET_STREAM_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePatient(@PathVariable("id") Long id,
                                            @RequestParam("nom") String nom,
                                            @RequestParam("prenom") String prenom,
                                            @RequestParam("date_naissance") String dateNaissance,
                                            @RequestParam("age") int age,
                                            @RequestParam("email") String email,
                                            @RequestParam("sexe") String sexe,
                                            @RequestParam("adresse") String adresse,
                                            @RequestParam("tel") String tel,
                                            @RequestParam("goup_sanguin") String groupeSanguin,
                                            @RequestPart(value = "image", required = false) MultipartFile image) {
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setNom(nom);
        patientRequest.setPrenom(prenom);
        patientRequest.setDate_naissance(dateNaissance);
        patientRequest.setAge(age);
        patientRequest.setEmail(email);
        patientRequest.setSexe(sexe);
        patientRequest.setAdresse(adresse);
        patientRequest.setTel(tel);
        patientRequest.setGoup_sanguin(groupeSanguin);
        patientRequest.setImage(image);
    
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