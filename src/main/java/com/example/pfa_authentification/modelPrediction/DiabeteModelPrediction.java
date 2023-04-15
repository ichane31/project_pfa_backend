package com.example.pfa_authentification.modelPrediction;

import org.pmml4s.model.Model;

import java.util.Arrays;
import java.util.Map;

public class DiabeteModelPrediction {

    private final Model model = Model.fromFile(StrokeModePrediction.class.getClassLoader().getResource("static/model/modelDiabete.pmml").getFile());

    public Double getDiabetePredicValue(Map<String, Double> values) {
        Object[] valuesMap = Arrays.stream(model.inputNames())
                .map(values::get)
                .toArray();


        Object[] prediction = model.predict(valuesMap);
        String[] outputFieldNames = model.targetNames();

        System.out.println("outputFieldNames" + outputFieldNames[0]);

        // Retourner la valeur de la classe prédite
        //classe 0
        return (Double) prediction[1];
    }
}
