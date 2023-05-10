package com.example.pfa_backend.services;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.modelPrediction.StrokeModePrediction;
import com.example.pfa_backend.models.Medecin;
import com.example.pfa_backend.models.Patient;
import com.example.pfa_backend.models.Stroke;
import com.example.pfa_backend.payload.request.StrokeRequest;
import com.example.pfa_backend.repositories.StrokeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StrokeService {

    @Autowired
    private StrokeRepository strokeRepository;

    @Autowired
    private PatientService patientService;

    public List<Stroke> getAllStrokes() {
        return strokeRepository.findAll();
    }

    public Optional<Stroke> getStrokeById(Long id) {
        return strokeRepository.findById(id);
    }


    public List<Stroke> getStrokesByHypertension(Boolean hypertension) {
        return strokeRepository.findByHypertension(hypertension);
    }

    public List<Stroke> getStrokesByBmiLessThan(Double bmi) {
        return strokeRepository.findByBmiLessThan(bmi);
    }

    public List<Stroke> getStrokesByAgeRange(Integer ageStart, Integer ageEnd) {
        return strokeRepository.findByAgeGreaterThanEqualAndAgeLessThanEqual(ageStart, ageEnd);
    }

    public Long countStrokesBySex(String sex) {
        return strokeRepository.countBySex(sex);
    }

    public List<Stroke> getStrokesByPatient(Patient patient) {
        return strokeRepository.findByPatient(patient);
    }

    public List<Stroke> getStrokesByPatientId(Long patientId) {
        return strokeRepository.findByPatientId(patientId);
    }

    public List<Stroke> getStrokesByBmi(double bmi) {
        return strokeRepository.findByBmi(bmi);
    }

    public List<Stroke> getStrokesBySexe(int sexe) {
        return strokeRepository.findBySex(sexe);
    }

    public List<Stroke> getStrokesByResultat(double resultat) {
        return strokeRepository.findByResultat(resultat);
    }

    public Stroke saveStroke(Stroke stroke) {
        return strokeRepository.save(stroke);
    }

    public void deleteStroke(Long id) {
        strokeRepository.deleteById(id);
    }

    public Stroke predictStroke(StrokeRequest strokeRequest , Long id) throws NotFoundException {
        StrokeModePrediction strokeModePrediction = new StrokeModePrediction();
        Patient patient = patientService.getPatientById(id);
        if(patient==null) {
            throw new NotFoundException("Patient not found with id" +id);
        }
        Map<String, Double> values = new HashMap<>();
        Stroke stroke = new Stroke();
        values.put("age", (double) strokeRequest.getAge());
        stroke.setAge(strokeRequest.getAge());
        values.put("hypertension", (double) strokeRequest.getHypertension());
        stroke.setHypertension(strokeRequest.getHypertension());
        values.put("heart_disease", (double) strokeRequest.getHeart_disease());
        stroke.setHeart_disease(strokeRequest.getHeart_disease());
        values.put("avg_glucose_level", strokeRequest.getAvg_glucose_level());
        stroke.setAvg_glucose_level(strokeRequest.getAvg_glucose_level());
        values.put("bmi", strokeRequest.getBmi());
        stroke.setBmi(strokeRequest.getBmi());
        values.put("sex", (double) strokeRequest.getSex());
        stroke.setSex(strokeRequest.getSex());
        values.put("work_type", (double) strokeRequest.getWork_type());
        stroke.setWork_type(strokeRequest.getWork_type());
        values.put("Residence_type", (double) strokeRequest.getResidence_type());
        stroke.setResidence_type(strokeRequest.getResidence_type());
        values.put("smoking_status", (double) strokeRequest.getSmoking_status());
        stroke.setSmoking_status(strokeRequest.getSmoking_status());
        values.put("ever_married", (double) strokeRequest.getEver_married());
        stroke.setEver_married(strokeRequest.getEver_married());
        LocalDate today = LocalDate.now();
        stroke.setDate(today);
        stroke.setPatient(patient);

        double predicted = strokeModePrediction.getRandomPredicValue(values);

        stroke.setResultat(predicted);

        return stroke;

    }

    public long countDistinctPatients() {
        List<Stroke> strokes = strokeRepository.findAll();
        return strokes.stream()
                .map(Stroke::getPatient)
                .distinct()
                .count();
    }

    public  Long countStrokesByMedecin(Long medecin) {
        Long strokeCount = strokeRepository.countByPatient_MedecinTraitant_Id(medecin);
        return strokeCount;
    }


}
