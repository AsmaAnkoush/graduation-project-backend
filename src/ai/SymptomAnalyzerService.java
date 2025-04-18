package com.smartvax.ai;  // مهم أن يكون نفس اسم الباكيج!

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.converters.ConverterUtils.DataSource;

public class SymptomAnalyzerService {

    private Instances trainingData;
    private Classifier model;

    public SymptomAnalyzerService() throws Exception {
        DataSource source = new DataSource("src/main/resources/symptoms_dataset.arff");
        trainingData = source.getDataSet();
        trainingData.setClassIndex(1);

        model = new NaiveBayes();
        model.buildClassifier(trainingData);
    }

    public String analyzeSymptom(String symptomText) throws Exception {
        Instance newInstance = new DenseInstance(2);
        newInstance.setDataset(trainingData);

        newInstance.setValue(0, symptomText);

        double predictionIndex = model.classifyInstance(newInstance);
        return trainingData.classAttribute().value((int) predictionIndex);
    }
}
