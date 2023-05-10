package com.example.pfa_backend.modelPrediction;

import org.pmml4s.model.Model;

import java.util.Arrays;
import java.util.Map;

public class HypertensionModelPrediction {

    private final Model model = Model.fromFile(StrokeModePrediction.class.getClassLoader().getResource("static/model/modelHypertension.pmml").getFile());

    public Double getHypertensionPredicValue(Map<String, Double> values) {
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
