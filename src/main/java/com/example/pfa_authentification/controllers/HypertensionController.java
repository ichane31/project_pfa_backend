package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Diabete;
import com.example.pfa_authentification.models.Hypertension;
import com.example.pfa_authentification.payload.request.DiabeteRequest;
import com.example.pfa_authentification.payload.request.HypertensionRequest;
import com.example.pfa_authentification.services.HypertensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/hypertension")
public class HypertensionController {

    @Autowired
    private HypertensionService hypertensionService;

    @PostMapping("/{id}/predict")
    public ResponseEntity<?> predictHypertension(@RequestBody HypertensionRequest hypertensionRequest , @PathVariable Long id) {
        Hypertension predict = null;
        try {
            predict = hypertensionService.predictHypertension(hypertensionRequest,id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND );
        }
        hypertensionService.saveHypertention(predict);

        return new ResponseEntity<>(predict,HttpStatus.OK );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(hypertensionService.findAll(),HttpStatus.OK );

    }

    @GetMapping("/{id}")
    public Hypertension getById(@PathVariable Long id) {
        return hypertensionService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        hypertensionService.deleteById(id);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getByPatientId(@PathVariable Long patientId) {
        return new ResponseEntity<>(hypertensionService.getByPatientId(patientId),HttpStatus.OK );

    }
}
