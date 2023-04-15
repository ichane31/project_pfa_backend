package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.InvalidException;
import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.RendezVous;
import com.example.pfa_authentification.payload.request.RendezVousRequest;
import com.example.pfa_authentification.services.MedecinService;
import com.example.pfa_authentification.services.PatientService;
import com.example.pfa_authentification.services.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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

    @GetMapping
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
    public List<RendezVous> getByDate(@PathVariable LocalDate date) {
        return rendezVousService.getByDate(date);
    }

    @GetMapping("/medecin/{id}/date/{date}")
    public List<RendezVous> getByMedecinIdAndDate(@PathVariable Long id, @PathVariable LocalDate date) {
        Medecin medecin = medecinService.getMedecinById(id).get();
        return rendezVousService.getByMedecinIdAndDate(medecin, date);
    }

    @PostMapping("/")
    public ResponseEntity<RendezVous> saveRendezVous(@RequestBody RendezVousRequest rendezVousRequest,
                                                     @RequestParam Long medecinId,
                                                     @RequestParam Long patientId) {
        Medecin medecin = medecinService.getMedecinById(medecinId).get();
        Patient patient = patientService.getPatientById(patientId);

        if (medecin == null || patient == null) {
            return ResponseEntity.badRequest().build();
        }

        RendezVous rendezVous = rendezVousService.saveRendezVous(rendezVousRequest, medecin, patient);
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

    @GetMapping("/aujourdhui")
    public List<RendezVous> getRendezVousAujourdhui() {
        return rendezVousService.getRendezVousAujourdhui();
    }

    @GetMapping("/medecins/{medecinId}/rendezvous/aujourdhui")
    public List<RendezVous> getRendezVousAujourdhui(@PathVariable Long medecinId) {
        Medecin medecin = medecinService.getMedecinById(medecinId).get();
        LocalDate today = LocalDate.now();
        return rendezVousService.getByMedecinIdAndDate(medecin, today);
    }

    @GetMapping("/rendezvous/search")
    public ResponseEntity<List<RendezVous>> searchRendezVousByMotif(@RequestParam String motif) {
        List<RendezVous> rendezVousList = rendezVousService.findByMotifContainingIgnoreCase(motif);
        //List<RendezVousResponse> responseList = rendezVousList.stream()
             //   .map(rendezVousMapper::rendezVousToRendezVousResponse)
              //  .collect(Collectors.toList());
        return ResponseEntity.ok(rendezVousList);
    }


}
