package com.bzu.smartvax.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@Service
@Transactional
public class AIAnalyzerService {

    private Instances datasetStructure;
    private Classifier classifier;

    public static final Map<String, String> VACCINE_NAME_MAPPING = new HashMap<>();

    static {
        VACCINE_NAME_MAPPING.put("السل (الدرن)", "السل_الرئوي_الدرن");
        VACCINE_NAME_MAPPING.put("التهاب الكبد البائي (الجرعة الأولى)", "التهاب_الكبد_البائي_الجرعة_الأولى");
        VACCINE_NAME_MAPPING.put("شلل الأطفال بالحقن (الجرعة الأولى)", "شلل_الأطفال_بالحقن_الجرعة_الأولى");
        VACCINE_NAME_MAPPING.put("التطعيم الخماسي (الجرعة الأولى)", "التطعيم_الخماسي_الجرعة_الأولى");
        VACCINE_NAME_MAPPING.put(
            "لقاح الحصبة الحصبة الألمانية النكاف (الجرعة الأولى)",
            "لقاح_الحصبة_الحصبة_الألمانية_النكاف_الجرعة_الأولى"
        );
        VACCINE_NAME_MAPPING.put("شلل الأطفال الفموي (الجرعة الأولى)", "شلل_الأطفال_الفموي_الجرعة_الأولى");
        // اذا في اسم مختلف بالداتا بيس أو واجهتك (أو تريد إضافة جرعات أخرى): اضفهم بنفس النمط
    }

    public AIAnalyzerService() throws Exception {
        loadModel();
    }

    private void loadModel() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("groups_sideeffects_palestine.arff");

        if (inputStream == null) {
            throw new RuntimeException("❌ ARFF file not found in resources!");
        }

        DataSource source = new DataSource(inputStream);
        Instances data = source.getDataSet();

        if (data == null) {
            throw new RuntimeException("❌ Failed to load ARFF data from InputStream");
        }

        data.setClassIndex(data.numAttributes() - 1); // عمود التشخيص

        this.datasetStructure = new Instances(data, 0);
        this.classifier = new J48();
        this.classifier.buildClassifier(data);
    }

    /**
     * توقع التشخيص بناءً على التطعيم وقائمة الأعراض.
     *
     * @param vaccineName اسم التطعيم كما في الداتا بيز (مثال: "السل (الدرن)")
     * @param symptomsMap خريطة: اسم العرض كما في الـ ARFF -> yes/no (مثال: "الحمى" -> "yes")
     * @return التشخيص المتوقع
     */
    public String predictDiagnosis(String vaccineName, Map<String, String> symptomsMap) throws Exception {
        // حول اسم التطعيم إذا موجود في الماب، إذا مش موجود أرسله زي ما هو (عشان تدعم حالات الاختبار)
        String arffVaccineName = convertVaccineNameToArffFormat(vaccineName);
        Instance instance = new DenseInstance(datasetStructure.numAttributes());
        instance.setDataset(datasetStructure);

        // أول عمود: اسم التطعيم (يجب أن يكون مطابق بالضبط لما في ARFF)
        instance.setValue(0, arffVaccineName);

        // أعمدة الأعراض (من 1 حتى آخر عمود -2)
        for (int i = 1; i < datasetStructure.numAttributes() - 1; i++) {
            String attrName = datasetStructure.attribute(i).name();
            String value = symptomsMap.getOrDefault(attrName, "no"); // افتراضي "no" إذا مش موجود
            instance.setValue(i, value);
        }

        // التشخيص: يتم استنتاجه
        double result = classifier.classifyInstance(instance);
        return datasetStructure.classAttribute().value((int) result);
    }

    // دالة لمطابقة اسم التطعيم في ARFF إذا كان عندك تحويل من قاعدة البيانات
    private String convertVaccineNameToArffFormat(String name) {
        //        // هنا ضع التحويلات حسب ما هو معرف في ملف ARFF لديك
        //        if (name.contains("السل")) return "السل_الرئوي_الدرن";
        //        if (name.contains("التهاب الكبد البائي")) return "التهاب_الكبد_البائي_الجرعة_الأولى";
        //        if (name.contains("شلل الأطفال بالحقن")) return "شلل_الأطفال_بالحقن_الجرعة_الأولى";
        //        if (name.contains("التطعيم الخماسي")) return "التطعيم_الخماسي_الجرعة_الأولى";
        //        if (name.contains("الحصبة")) return "لقاح_الحصبة_الحصبة_الألمانية_النكاف_الجرعة_الأولى";
        //        if (name.contains("شلل الأطفال الفموي")) return "شلل_الأطفال_الفموي_الجرعة_الأولى";
        //        // ... أكمل حسب اللقاحات الموجودة عندك
        return name.trim().replace(" ", "_").replace("(", "").replace(")", "");
    }

    /**
     * جلب قائمة أسماء جميع الأعراض بالضبط كما تظهر في ملف ARFF (تستخدمها خدمات أخرى).
     * يبدأ من العمود 1 (بعد التطعيم) وينتهي قبل آخر عمود (التشخيص).
     */
    public List<String> getSymptomsAttributes() {
        List<String> attrs = new ArrayList<>();
        for (int i = 1; i < datasetStructure.numAttributes() - 1; i++) {
            attrs.add(datasetStructure.attribute(i).name());
        }
        return attrs;
    }
}
