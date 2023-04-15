package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Diabete;
import com.example.pfa_authentification.models.Hypertension;
import com.example.pfa_authentification.models.Stroke;
import com.example.pfa_authentification.payload.request.DiabeteRequest;
import com.example.pfa_authentification.payload.request.StrokeRequest;
import com.example.pfa_authentification.services.DiabeteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/diabetes")
public class DiabeteController {

    @Autowired
    private DiabeteService diabeteService;

    @GetMapping("/bmi/{bmi}")
    public ResponseEntity<List<Diabete>> getDiabetesByBmi(@PathVariable double bmi) {
        List<Diabete> diabetes= diabeteService.getDiabetesByBmi(bmi);
        return new ResponseEntity<>(diabetes,HttpStatus.FOUND);

    }

    @GetMapping("/gen-hlth/{genHlth}")
    public ResponseEntity<List<Diabete>> getDiabetesByGenHlth(@PathVariable int genHlth) {
        List<Diabete> diabetes= diabeteService.getDiabetesByGenHlth(genHlth);
        return new ResponseEntity<>(diabetes,HttpStatus.FOUND);

    }

    @GetMapping("/no-alcohol")
    public ResponseEntity<List<Diabete>> getDiabetesByNoAlcoholConsumption(@RequestBody int v) {
        List<Diabete>diabetes= diabeteService.getDiabetesByNoAlcoholConsumption(v);
        return new ResponseEntity<>(diabetes,HttpStatus.FOUND);
    }

    @GetMapping("/{sex}/{age}")
    public ResponseEntity<List<Diabete>> getDiabetesBySexAndAge(@PathVariable int sex, @PathVariable int age) {
        List<Diabete> diabetes= diabeteService.getDiabetesBySexAndAge(sex, age);
        return new ResponseEntity<>(diabetes,HttpStatus.FOUND);

    }

    @GetMapping("/count-phys-hlth/{physHlth}")
    public long countDiabetesByPhysHlth(@PathVariable int physHlth) {
        return diabeteService.countDiabetesByPhysHlth(physHlth);
    }

    @PostMapping("/{id}/predict")
    public ResponseEntity<?> predictStroke(@RequestBody DiabeteRequest diabeteRequest , @PathVariable Long id) {
        Diabete predict = null;
        try {
            predict = diabeteService.predictDiaete(diabeteRequest,id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND );
        }
        diabeteService.saveDiabete(predict);

        return new ResponseEntity<>(predict,HttpStatus.CREATED );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Diabete>> getByPatientId(@PathVariable Long patientId) {
        List<Diabete> diabetes= diabeteService.getByPatientId(patientId);
        return new ResponseEntity<>(diabetes,HttpStatus.FOUND);

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        diabeteService.deleteById(id);
    }

}