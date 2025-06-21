package com.smartvax.ai;

import java.io.InputStream;
import java.util.Map;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class SymptomAnalyzerService {

    private Instances trainingData;
    private Classifier model;

    public SymptomAnalyzerService() throws Exception {
        // استخدم تحميل من resources وليس من المسار المباشر
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("groups_sideeffects_palestine.arff");
        if (inputStream == null) {
            throw new RuntimeException("❌ ARFF file not found in resources!");
        }
        DataSource source = new DataSource(inputStream);
        trainingData = source.getDataSet();

        // تأكد أن عمود التصنيف هو الأخير (كما في ARFF)
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        model = new NaiveBayes();
        model.buildClassifier(trainingData);
    }

    /**
     * توقع التشخيص بناءً على اسم التطعيم وكل الأعراض.
     * Map: اسم العرض -> yes/no
     */
    public String analyzeSymptom(String vaccineName, Map<String, String> symptomsMap) throws Exception {
        Instance newInstance = new DenseInstance(trainingData.numAttributes());
        newInstance.setDataset(trainingData);

        // أول عمود هو اسم التطعيم
        newInstance.setValue(0, vaccineName);

        // باقي الأعمدة هي الأعراض بالترتيب
        for (int i = 1; i < trainingData.numAttributes() - 1; i++) {
            String attrName = trainingData.attribute(i).name();
            String value = symptomsMap.getOrDefault(attrName, "no"); // افتراضي "no" إذا مش موجود
            newInstance.setValue(i, value);
        }

        double predictionIndex = model.classifyInstance(newInstance);
        return trainingData.classAttribute().value((int) predictionIndex);
    }
}
