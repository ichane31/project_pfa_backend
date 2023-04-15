package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Stroke;
import com.example.pfa_authentification.payload.request.StrokeRequest;
import com.example.pfa_authentification.services.StrokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/strokes")
public class StrokeController {

    @Autowired
    private StrokeService strokeService;

    @GetMapping("/all")
    public ResponseEntity<List<Stroke>> getAllStrokes() {
        List<Stroke> strokes = strokeService.getAllStrokes();
        return new ResponseEntity<>(strokes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stroke> getStrokeById(@PathVariable Long id) {
        Optional<Stroke> stroke = strokeService.getStrokeById(id);
        return stroke.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/resultat/{resultat}")
    public ResponseEntity<List<Stroke>> getStrokesByResultat(@PathVariable double resultat) {
        List<Stroke> strokes = strokeService.getStrokesByResultat(resultat);
        return new ResponseEntity<>(strokes, HttpStatus.OK);
    }

    @GetMapping("/hypertension/{hypertension}")
    public ResponseEntity<List<Stroke>> getStrokesByHypertension(@PathVariable Boolean hypertension) {
        List<Stroke> strokes = strokeService.getStrokesByHypertension(hypertension);
        return new ResponseEntity<>(strokes, HttpStatus.OK);
    }

    @GetMapping("/bmi/{bmi}")
    public ResponseEntity<List<Stroke>> getStrokesByBmi(@PathVariable Double bmi) {
        List<Stroke> strokes = strokeService.getStrokesByBmiLessThan(bmi);
        return new ResponseEntity<>(strokes, HttpStatus.OK);
    }

    @GetMapping("/age/{ageStart}/{ageEnd}")
    public ResponseEntity<List<Stroke>> getStrokesByAgeRange(@PathVariable Integer ageStart, @PathVariable Integer ageEnd) {
        List<Stroke> strokes = strokeService.getStrokesByAgeRange(ageStart, ageEnd);
        return new ResponseEntity<>(strokes, HttpStatus.OK);
    }

    @GetMapping("/sex/{sex}")
    public ResponseEntity<Long> countStrokesBySex(@PathVariable String sex) {
        Long count = strokeService.countStrokesBySex(sex);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Stroke>> getStrokesByPatientId(@PathVariable Long patientId) {
        List<Stroke> strokes = strokeService.getStrokesByPatientId(patientId);
        return new ResponseEntity<>(strokes, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Stroke> saveStroke(@RequestBody Stroke stroke) {
        Stroke savedStroke = strokeService.saveStroke(stroke);
        return new ResponseEntity<>(savedStroke, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStroke(@PathVariable Long id) {
        strokeService.deleteStroke(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/predict")
    public ResponseEntity<?> predictStroke(@RequestBody StrokeRequest stroke , @PathVariable Long id) {
        Stroke predict = null;
        try {
            predict = strokeService.predictStroke(stroke,id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND );
        }
        strokeService.saveStroke(predict);

        return new ResponseEntity<>(predict,HttpStatus.CREATED );
    }
}