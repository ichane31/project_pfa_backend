package com.example.pfa_authentification.services;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.modelPrediction.HypertensionModelPrediction;
import com.example.pfa_authentification.models.Hypertension;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.Stroke;
import com.example.pfa_authentification.payload.request.HypertensionRequest;
import com.example.pfa_authentification.repositories.HypertensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HypertensionService {

    @Autowired
    private HypertensionRepository hypertensionRepository;

    @Autowired
    private PatientService patientService;

    public List<Hypertension> findPatientsByTrestbpsGreaterThan(Integer trestbps) {
        return hypertensionRepository.findByTrestbpsGreaterThan(trestbps);
    }

    public List<Hypertension> findPatientsByAge(Integer ageStart, Integer ageEnd) {
        return hypertensionRepository.findByAgeGreaterThanEqualAndAgeLessThanEqual(ageStart, ageEnd);
    }

    public Long countPatientsByCp(Integer cp) {
        return hypertensionRepository.countByCp(cp);
    }

    public Hypertension predictHypertension(HypertensionRequest hypertensionRequest , Long id) throws NotFoundException {
        HypertensionModelPrediction hypertensionModelPrediction = new HypertensionModelPrediction();
        Patient patient = patientService.getPatientById(id);
        if(patient==null) {
            throw new NotFoundException("Patient not found with id" +id);
        }
        Map<String, Double> values = new HashMap<>();
        Hypertension hypertension = new Hypertension();
        values.put("age", (double) hypertensionRequest.getAge());
        hypertension.setAge(hypertensionRequest.getAge());
        values.put("cp", (double) hypertensionRequest.getCp());
        hypertension.setCp(hypertensionRequest.getCp());
        values.put("trestbps", (double) hypertensionRequest.getTrestbps());
        hypertension.setTrestbps(hypertensionRequest.getTrestbps());
        values.put("chol", (double) hypertensionRequest.getChol());
        hypertension.setChol(hypertensionRequest.getChol());
        values.put("fbs", (double) hypertensionRequest.getFbs());
        hypertension.setFbs(hypertensionRequest.getFbs());
        values.put("sex", (double) hypertensionRequest.getSexe());
        hypertension.setSexe(hypertensionRequest.getSexe());
        values.put("restecg", (double) hypertensionRequest.getRestecg());
        hypertension.setRestecg(hypertensionRequest.getRestecg());
        values.put("thalach", (double) hypertensionRequest.getThalach());
        hypertension.setThalach(hypertensionRequest.getThalach());
        values.put("exang", (double) hypertensionRequest.getExang());
        hypertension.setExang(hypertensionRequest.getExang());
        values.put("oldpeak",  hypertensionRequest.getOldpeak());
        hypertension.setOldpeak(hypertensionRequest.getOldpeak());
        values.put("slope", (double) hypertensionRequest.getSlope());
        hypertension.setSlope(hypertensionRequest.getSlope());
        values.put("ca", (double) hypertensionRequest.getCa());
        hypertension.setCa(hypertensionRequest.getCa());
        values.put("thal", (double) hypertensionRequest.getThal());
        hypertension.setThal(hypertensionRequest.getThal());

        hypertension.setPatient(patient);

        double predicted = hypertensionModelPrediction.getHypertensionPredicValue(values);

        hypertension.setResultat(predicted);

        return hypertension;

    }

    public Hypertension saveHypertention(Hypertension predict) {
        return hypertensionRepository.save(predict);
    }

    public List<Hypertension> findAll() {
        return hypertensionRepository.findAll();
    }

    public Hypertension findById(Long id) {
        return hypertensionRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        hypertensionRepository.deleteById(id);
    }

    public List<Hypertension> getByPatientId(Long patientId) {
        return hypertensionRepository.findAllByPatient_Id(patientId);
    }
}
