package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.*;
import com.example.pfa_backend.payload.request.PatientTraitementRequest;
import com.example.pfa_backend.payload.response.PatientTraitementResponse;
import com.example.pfa_backend.services.PatientTraitementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patient-traitements")
public class PatientTraitementController {

    @Autowired
    private PatientTraitementService patientTraitementService;

    @GetMapping("/{id}")
    public ResponseEntity<PatientTraitement> getById(@PathVariable Long id)  {
        PatientTraitement patientTraitement = null;
        try {
            patientTraitement = patientTraitementService.getById(id);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patientTraitement);
    }

    @GetMapping
    public ResponseEntity<List<PatientTraitement>> getAll() {
        List<PatientTraitement> patientTraitements = patientTraitementService.getAll();
        return ResponseEntity.ok(patientTraitements);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientTraitement>> getByPatientId(@PathVariable Long patientId) {
        List<PatientTraitement> patientTraitements = patientTraitementService.getByPatientId(patientId);
        return ResponseEntity.ok(patientTraitements);
    }

    @GetMapping("/traitement/{traitementId}")
    public ResponseEntity<List<PatientTraitement>> getByTraitementId(@PathVariable Long traitementId) {
        List<PatientTraitement> patientTraitements = patientTraitementService.getByTraitementId(traitementId);
        return ResponseEntity.ok(patientTraitements);
    }

    @GetMapping("/start-date-between")
    public ResponseEntity<List<PatientTraitement>> getByStartDateBetween(
            @RequestParam("start-date") @Valid String startDate,
            @RequestParam("end-date") @Valid String endDate) {
        List<PatientTraitement> patientTraitements = patientTraitementService.getByStartDateBetween(startDate, endDate);
        return ResponseEntity.ok(patientTraitements);
    }

    @GetMapping("/end-date-between")
    public ResponseEntity<List<PatientTraitement>> getByEndDateBetween(
            @RequestParam("start-date") @Valid String startDate,
            @RequestParam("end-date") @Valid String endDate) {
        List<PatientTraitement> patientTraitements = patientTraitementService.getByEndDateBetween(startDate, endDate);
        return ResponseEntity.ok(patientTraitements);
    }

    @PostMapping("/{id}/{id_trait}")
    public ResponseEntity<PatientTraitement> create(
            @RequestBody @Valid PatientTraitementRequest patientTraitementRequest,
            @PathVariable("id") Long patientId,
            @PathVariable("id_trait") Long traitementId) throws NotFoundException {
        PatientTraitement patientTraitement = patientTraitementService.create(patientTraitementRequest, patientId, traitementId);
        return ResponseEntity.status(HttpStatus.OK).body(patientTraitement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        patientTraitementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientTraitement> update(
            @PathVariable Long id,
            @RequestBody @Valid PatientTraitementRequest patientTraitementRequest) throws NotFoundException {
        PatientTraitement patientTraitement = patientTraitementService.update(id, patientTraitementRequest);
        return ResponseEntity.ok(patientTraitement);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<PatientTraitementResponse>> getAllTraitementByPatientId(@PathVariable Long id) throws NotFoundException {
        List<PatientTraitement> patientTraitements = patientTraitementService.getByPatientId(id);
        List<PatientTraitementResponse> responses = new ArrayList<>();
        for (PatientTraitement patientTraitement : patientTraitements) {
            Traitement traitement = patientTraitement.getTraitement();
            String startDate = patientTraitement.getStartDate();
            String endDate = patientTraitement.getEndDate();
            responses.add(new PatientTraitementResponse(traitement, startDate, endDate));
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/patients")
    public ResponseEntity<List<PatientTraitementResponse>> getAllPatientByTraitementId(@PathVariable Long id) throws NotFoundException {
        List<PatientTraitement> patientTraitements = patientTraitementService.getByTraitementId(id);
        List<PatientTraitementResponse> responses = new ArrayList<>();
        for (PatientTraitement patientTraitement : patientTraitements) {
            Traitement traitement = patientTraitement.getTraitement();
            Patient patient = patientTraitement.getPatient();
            String startDate = patientTraitement.getStartDate();
            String endDate = patientTraitement.getEndDate();
            String evaluation = patientTraitement.getEvaluationTraitement();
            responses.add(new PatientTraitementResponse(traitement,patient, startDate, endDate ,evaluation));
        }
        return ResponseEntity.ok(responses);
    }

}