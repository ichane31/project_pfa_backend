package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.InvalidException;
import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.Traitement;
import com.example.pfa_backend.payload.request.TraitementRequest;
import com.example.pfa_backend.payload.response.TraitementResponse;
import com.example.pfa_backend.services.TraitementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/traitements")
public class TraitementController {

    @Autowired
    private TraitementService traitementService;

    @GetMapping("/{id}")
    public ResponseEntity<Traitement> getById(@PathVariable("id") Long id) {
        Traitement traitement = traitementService.getById(id);
        if (traitement == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(traitement, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Traitement>> getAll() {
        List<Traitement> traitements = traitementService.getAll();
        return new ResponseEntity<>(traitements, HttpStatus.OK);
    }

    @PostMapping("/{medecinId}/add")
    public ResponseEntity<?> create( @PathVariable("medecinId") Long medecinId,@Valid @RequestBody TraitementRequest traitementRequest) {
        Traitement traitement = null;
        try {
            traitement = traitementService.create(medecinId,traitementRequest);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(traitement, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        traitementService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Traitement> update(@PathVariable("id") Long id,
                                             @Valid @RequestBody TraitementRequest traitementRequest) throws NotFoundException {
        Traitement updatedTraitement = traitementService.update(id, traitementRequest);
        return new ResponseEntity<>(updatedTraitement, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String description) {
        List<Traitement> traitements;
        Traitement traitement;
        if (name != null) {
            traitement = traitementService.getByName(name);
            return new ResponseEntity<>(traitement, HttpStatus.OK);

        } else if (description != null) {
            traitements = traitementService.getByDescriptionContaining(description);
            return new ResponseEntity<>(traitements, HttpStatus.OK);

        } else {
            traitements = traitementService.getAll();
            return new ResponseEntity<>(traitements, HttpStatus.OK);

        }
    }

    @GetMapping("/medecins/{medecinId}")
    public ResponseEntity<List<TraitementResponse>> getAllByMedecinId(@PathVariable("medecinId") Long medecinId) {
        List<TraitementResponse> traitements;
        try {
            traitements = traitementService.getAllByMedecinId(medecinId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(traitements, HttpStatus.OK);
    }

}
