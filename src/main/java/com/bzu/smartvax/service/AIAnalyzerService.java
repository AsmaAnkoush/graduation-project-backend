package com.bzu.smartvax.service;

import java.io.InputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

@Service
@Transactional
public class AIAnalyzerService {

    private Instances datasetStructure;
    private Classifier classifier;

    public AIAnalyzerService() throws Exception {
        loadModel();
    }

    private void loadModel() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("symptoms_with_vaccine.arff");

        if (inputStream == null) {
            throw new RuntimeException("❌ ARFF file not found in resources!");
        }

        // ✅ استخدم ArffLoader بدلاً من DataSource
        ArffLoader loader = new ArffLoader();
        loader.setSource(inputStream); // ⛔️ لو تجاهلتها: "No source has been specified"
        Instances data = loader.getDataSet();

        if (data == null) {
            throw new RuntimeException("❌ Failed to load ARFF data from InputStream");
        }

        data.setClassIndex(data.numAttributes() - 1); // عمود التشخيص

        this.datasetStructure = new Instances(data, 0);
        this.classifier = new J48(); // يمكنك استخدام NaiveBayes أو غيره
        this.classifier.buildClassifier(data);
    }

    public String predictDiagnosis(
        String vaccineName,
        boolean fever,
        boolean redness,
        boolean swelling,
        boolean rash,
        boolean headache,
        boolean vomiting,
        boolean fatigue,
        boolean lossOfAppetite
    ) throws Exception {
        Instance instance = new DenseInstance(datasetStructure.numAttributes());
        instance.setDataset(datasetStructure);

        instance.setValue(0, vaccineName);
        instance.setValue(1, fever ? "yes" : "no");
        instance.setValue(2, redness ? "yes" : "no");
        instance.setValue(3, swelling ? "yes" : "no");
        instance.setValue(4, rash ? "yes" : "no");
        instance.setValue(5, headache ? "yes" : "no");
        instance.setValue(6, vomiting ? "yes" : "no");
        instance.setValue(7, fatigue ? "yes" : "no");
        instance.setValue(8, lossOfAppetite ? "yes" : "no");

        double result = classifier.classifyInstance(instance);
        return datasetStructure.classAttribute().value((int) result);
    }
}
