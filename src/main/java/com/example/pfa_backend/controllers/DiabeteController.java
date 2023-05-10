package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.Diabete;
import com.example.pfa_backend.payload.request.DiabeteRequest;
import com.example.pfa_backend.services.DiabeteService;
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

    @GetMapping("/all")
    public ResponseEntity<List<Diabete>> getAllDiabetes() {
        List<Diabete> diabetes = diabeteService.getAllDiabetes();
        return new ResponseEntity<>(diabetes, HttpStatus.OK);
    }

    @GetMapping("/bmi/{bmi}")
    public ResponseEntity<List<Diabete>> getDiabetesByBmi(@PathVariable double bmi) {
        List<Diabete> diabetes= diabeteService.getDiabetesByBmi(bmi);
        return new ResponseEntity<>(diabetes,HttpStatus.OK);

    }

    @GetMapping("/gen-hlth/{genHlth}")
    public ResponseEntity<List<Diabete>> getDiabetesByGenHlth(@PathVariable int genHlth) {
        List<Diabete> diabetes= diabeteService.getDiabetesByGenHlth(genHlth);
        return new ResponseEntity<>(diabetes,HttpStatus.OK);

    }

    @GetMapping("/no-alcohol")
    public ResponseEntity<List<Diabete>> getDiabetesByNoAlcoholConsumption(@RequestBody int v) {
        List<Diabete>diabetes= diabeteService.getDiabetesByNoAlcoholConsumption(v);
        return new ResponseEntity<>(diabetes,HttpStatus.OK);
    }

    @GetMapping("/{sex}/{age}")
    public ResponseEntity<List<Diabete>> getDiabetesBySexAndAge(@PathVariable int sex, @PathVariable int age) {
        List<Diabete> diabetes= diabeteService.getDiabetesBySexAndAge(sex, age);
        return new ResponseEntity<>(diabetes,HttpStatus.OK);

    }

    @GetMapping("/count-phys-hlth/{physHlth}")
    public long countDiabetesByPhysHlth(@PathVariable int physHlth) {
        return diabeteService.countDiabetesByPhysHlth(physHlth);
    }

    @PostMapping("/{id}/predict")
    public ResponseEntity<?> predictDiabete(@RequestBody DiabeteRequest diabeteRequest , @PathVariable Long id) {
        Diabete predict = null;
        try {
            predict = diabeteService.predictDiabete(diabeteRequest,id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND );
        }
        diabeteService.saveDiabete(predict);

        return new ResponseEntity<>(predict,HttpStatus.CREATED );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Diabete>> getByPatientId(@PathVariable Long patientId) {
        List<Diabete> diabetes= diabeteService.getByPatientId(patientId);
        return new ResponseEntity<>(diabetes,HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        diabeteService.deleteById(id);
    }

    @GetMapping("/countByMedecin/{medecinId}")
    public long countDiabetesByMedecin(@PathVariable Long medecinId) {
        return diabeteService.countDiabetesByMedecin(medecinId);
    }

    @GetMapping("/countDistinctPatients")
    public long countDistinctPatients() {
        return diabeteService.countDistinctPatients();
    }

}
