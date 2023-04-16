package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.HoraireTravail;
import com.example.pfa_authentification.services.HoraireTravailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/horaires")
public class HoraireTravailController {

    @Autowired
    private HoraireTravailService horaireTravailService;

    @GetMapping
    public List<HoraireTravail> getAllHoraires() {
        return horaireTravailService.getAllHoraires();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoraireTravail> getHoraireById(@PathVariable("id") Long id) throws NotFoundException {
        HoraireTravail horaireTravail = horaireTravailService.getHoraireById(id);
        return new ResponseEntity<>(horaireTravail, HttpStatus.OK);
    }

    @PostMapping("/{medecinId}/add")
    public ResponseEntity<HoraireTravail> ajouterHoraireTravail(@PathVariable("medecinId") Long medecinId,
                                                                @RequestBody HoraireTravail horaireTravail) throws NotFoundException {
        horaireTravailService.ajouterHoraireTravail(medecinId, horaireTravail);
        return new ResponseEntity<>(horaireTravail, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HoraireTravail> updateHoraire(@PathVariable("id") Long id,
                                                        @RequestBody HoraireTravail horaireTravailDetails) throws NotFoundException {
        HoraireTravail updatedHoraire = horaireTravailService.updateHoraire(id, horaireTravailDetails);
        return new ResponseEntity<>(updatedHoraire, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHoraire(@PathVariable("id") Long id) throws NotFoundException {
        horaireTravailService.deleteHoraire(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/disponible")
    public ResponseEntity<Boolean> isHoraireDisponible(@RequestParam("medecinId") Long medecinId,
                                                       @RequestParam("jourSemaine") DayOfWeek jourSemaine,
                                                       @RequestParam("heureDebut") LocalTime heureDebut,
                                                       @RequestParam("heureFin") LocalTime heureFin) {
        boolean isDisponible = horaireTravailService.isHoraireAvailable(medecinId, jourSemaine, heureDebut, heureFin);
        return new ResponseEntity<>(isDisponible, HttpStatus.OK);
    }

    @GetMapping("/{medecinId}/{jour}")
    public ResponseEntity<List<HoraireTravail>> getHorairesByMedecinAndDay(@PathVariable Long medecinId, @PathVariable DayOfWeek jour) {
        List<HoraireTravail> horaires = horaireTravailService.getHorairesByMedecinAndDay(medecinId, jour);
        return new ResponseEntity<>(horaires, HttpStatus.OK);
    }

}
