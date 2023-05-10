package com.example.pfa_authentification.services;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.modelPrediction.DiabeteModelPrediction;
import com.example.pfa_authentification.models.Diabete;
import com.example.pfa_authentification.models.Patient;
import com.example.pfa_authentification.models.Stroke;
import com.example.pfa_authentification.payload.request.DiabeteRequest;
import com.example.pfa_authentification.repositories.DiabeteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiabeteService {
    @Autowired
    private DiabeteRepository diabeteRepository;

    @Autowired
    private PatientService patientService;

    public List<Diabete> getAllDiabetes() {
        return diabeteRepository.findAll();
    }

    public List<Diabete> getDiabetesByBmi(double bmi) {
        return diabeteRepository.findAllByBmiGreaterThan(bmi);
    }

    public List<Diabete> getDiabetesByGenHlth(int genHlth) {
        return diabeteRepository.findAllByGenHlth(genHlth);
    }

    public List<Diabete> getDiabetesByNoAlcoholConsumption(int v) {
        return diabeteRepository.findAllByHvyAlcoholConsump(v);
    }
    public List<Diabete> getDiabetesBySexAndAge(int sex, int age) {
        return diabeteRepository.findAllBySexAndAge(sex, age);
    }
    public long countDiabetesByPhysHlth(int physHlth) {
        return diabeteRepository.countByPhysHlthLessThan(physHlth);
    }

    public Diabete predictDiabete(DiabeteRequest diabeteRequest , Long id) throws NotFoundException {
        DiabeteModelPrediction diabeteModelPrediction = new DiabeteModelPrediction();
        Patient patient = patientService.getPatientById(id);
        if(patient==null) {
            throw new NotFoundException("Patient not found with id" +id);
        }
        Map<String, Double> values = new HashMap<>();
        Diabete diabete = new Diabete();
        values.put("Age", (double) diabeteRequest.getAge());
        diabete.setAge(diabeteRequest.getAge());
        values.put("HighBP", (double) diabeteRequest.getHighBP());
        diabete.setHighBP(diabeteRequest.getHighBP());
        values.put("HighChol", (double) diabeteRequest.getHighChol());
        diabete.setHighChol(diabeteRequest.getHighChol());
        values.put("CholCheck", (double) diabeteRequest.getCholCheck());
        diabete.setCholCheck(diabeteRequest.getCholCheck());
        values.put("BMI", diabeteRequest.getBmi());
        diabete.setBmi(diabeteRequest.getBmi());
        values.put("Sex", (double) diabeteRequest.getSex());
        diabete.setSex(diabeteRequest.getSex());
        values.put("Smoker", (double) diabeteRequest.getSmoker());
        diabete.setSmoker(diabeteRequest.getSmoker());
        values.put("Stroke", (double) diabeteRequest.getStroke());
        diabete.setStroke(diabeteRequest.getStroke());
        values.put("HeartDiseaseorAttack", (double) diabeteRequest.getHeartDiseaseorAttack());
        diabete.setHeartDiseaseorAttack(diabeteRequest.getHeartDiseaseorAttack());
        values.put("PhysActivity", (double) diabeteRequest.getPhysActivity());
        diabete.setPhysActivity(diabeteRequest.getPhysActivity());
        values.put("Fruits", (double) diabeteRequest.getFruits());
        diabete.setFruits(diabeteRequest.getFruits());
        values.put("Veggies", (double) diabeteRequest.getVeggies());
        diabete.setVeggies(diabeteRequest.getVeggies());
        values.put("HvyAlcoholConsump", (double) diabeteRequest.getHvyAlcoholConsump());
        diabete.setHvyAlcoholConsump(diabeteRequest.getHvyAlcoholConsump());
        values.put("GenHlth", (double) diabeteRequest.getGenHlth());
        diabete.setGenHlth(diabeteRequest.getGenHlth());
        values.put("MentHlth", (double) diabeteRequest.getMentHlth());
        diabete.setMentHlth(diabeteRequest.getMentHlth());
        values.put("PhysHlth", (double) diabeteRequest.getPhysHlth());
        diabete.setPhysHlth(diabeteRequest.getPhysHlth());
        values.put("DiffWalk", (double) diabeteRequest.getDiffWalk());
        diabete.setDiffWalk(diabeteRequest.getDiffWalk());

        diabete.setPatient(patient);
        double predicted = diabeteModelPrediction.getDiabetePredicValue(values);

        diabete.setResultat(predicted);

        return diabete;

    }

    public Diabete saveDiabete(Diabete predict) {
        return diabeteRepository.save(predict);
    }

    public void deleteById(Long id) {
        diabeteRepository.deleteById(id);
    }

    public List<Diabete> getByPatientId(Long patientId) {
        return diabeteRepository.findAllByPatient_Id(patientId);
    }
}