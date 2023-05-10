package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.InvalidException;
import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.Medecin;
import com.example.pfa_backend.models.Patient;
import com.example.pfa_backend.models.RendezVous;
import com.example.pfa_backend.payload.request.RendezVousRequest;
import com.example.pfa_backend.services.MedecinService;
import com.example.pfa_backend.services.PatientService;
import com.example.pfa_backend.services.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getById(@PathVariable Long id) {
        RendezVous rendezVous = rendezVousService.getById(id);
        if (rendezVous != null) {
            return new ResponseEntity<>(rendezVous, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public List<RendezVous> getAll() {
        return rendezVousService.getAll();
    }

    @GetMapping("/medecin/{id}")
    public List<RendezVous> getByMedecinId(@PathVariable Long id) {
        return rendezVousService.getByMedecinId(id);
    }

    @GetMapping("/patient/{id}")
    public List<RendezVous> getByPatientId(@PathVariable Long id) {
        return rendezVousService.getByPatientId(id);
    }

    @GetMapping("/date/{date}")
    public List<RendezVous> getByDate(@PathVariable String date) {
        return rendezVousService.getByDate(date);
    }

    @GetMapping("/medecin/{id}/date/{date}")
    public List<RendezVous> getByMedecinIdAndDate(@PathVariable Long id, @RequestParam String date) {
        Medecin medecin = medecinService.getMedecinById(id).get();
        return rendezVousService.getByMedecinIdAndDate(medecin, date);
    }

    @PostMapping("/medecin/{idMedecin}/patient/{idPatient}")
    public ResponseEntity<RendezVous> saveRendezVous(@RequestBody RendezVousRequest rendezVousRequest,
                                                     @PathVariable Long idMedecin,
                                                     @PathVariable Long idPatient) {
        Medecin medecin = medecinService.getMedecinById(idMedecin).get();
        Patient patient = patientService.getPatientById(idPatient);

        if (medecin == null || patient == null) {
            return ResponseEntity.badRequest().build();
        }


        RendezVous rendezVous = null;
        try {
            rendezVous = rendezVousService.saveRendezVous(rendezVousRequest, medecin, patient);
        } catch (InvalidException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(rendezVous);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        rendezVousService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRendezVous(@PathVariable Long id, @Valid @RequestBody RendezVousRequest rendezVousRequest) {
        RendezVous rendezVous = null;
        try {
            rendezVous = rendezVousService.updateRendezVous(id, rendezVousRequest);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NO_CONTENT);
        } catch (InvalidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(rendezVous);
    }
    @PutMapping("/{id}/canceled")
    public ResponseEntity<?> cancelRendezVous(@PathVariable Long id ) {
        RendezVous rendezVous = null;
        try {
            rendezVous = rendezVousService.canceledRendezVous(id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (InvalidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(rendezVous);
    }

    @PutMapping("/{id}/confirmed")
    public ResponseEntity<?> confirmeRendezVous(@PathVariable Long id ) {
        RendezVous rendezVous = null;
        try {
            rendezVous = rendezVousService.confirmedRendezVous(id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (InvalidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/aujourdhui")
    public List<RendezVous> getRendezVousAujourdhui() {
        return rendezVousService.getRendezVousAujourdhui();
    }

    @GetMapping("/medecins/{medecinId}/rendezvous/aujourdhui")
    public List<RendezVous> getRendezVousAujourdhui(@PathVariable Long medecinId) {
        Medecin medecin = medecinService.getMedecinById(medecinId).get();
        LocalDate today = LocalDate.now();
        String date = today.toString();
        return rendezVousService.getByMedecinIdAndDate(medecin, date);
    }

    @GetMapping("/rendezvous/search")
    public ResponseEntity<List<RendezVous>> searchRendezVousByMotif(@RequestParam String motif) {
        List<RendezVous> rendezVousList = rendezVousService.findByMotifContainingIgnoreCase(motif);
        //List<RendezVousResponse> responseList = rendezVousList.stream()
             //   .map(rendezVousMapper::rendezVousToRendezVousResponse)
              //  .collect(Collectors.toList());
        return ResponseEntity.ok(rendezVousList);
    }

    @GetMapping("/heuresDisponibles/{id}")
    public ResponseEntity<?> getHeuresDisponibles(@PathVariable Long id, @RequestParam("date") String dateString) {
        Medecin medecin = medecinService.getMedecinById(id).get();
        //LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<LocalTime> disponibles = rendezVousService.getHeureDisponibles(medecin, dateString);
        return ResponseEntity.ok(disponibles);
    }


}
