package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.AIAnalyzer;
import com.bzu.smartvax.repository.AIAnalyzerRepository;
import com.bzu.smartvax.service.dto.AIAnalyzerDTO;
import com.bzu.smartvax.service.mapper.AIAnalyzerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Service Implementation for managing {@link AIAnalyzer}.
 */
@Service
@Transactional
public class AIAnalyzerService {

    private static final Logger LOG = LoggerFactory.getLogger(AIAnalyzerService.class);

    //    private final AIAnalyzerRepository aIAnalyzerRepository;

    //    private final AIAnalyzerMapper aIAnalyzerMapper;

    private Instances datasetStructure;

    private Classifier classifier;

    public AIAnalyzerService() throws Exception {
        //        this.aIAnalyzerRepository = aIAnalyzerRepository;
        //        this.aIAnalyzerMapper = aIAnalyzerMapper;
        loadModel();
    }

    //    /**
    //     * Save a aIAnalyzer.
    //     *
    //     * @param aIAnalyzerDTO the entity to save.
    //     * @return the persisted entity.
    //     */
    //    public AIAnalyzerDTO save(AIAnalyzerDTO aIAnalyzerDTO) {
    //        LOG.debug("Request to save AIAnalyzer : {}", aIAnalyzerDTO);
    //        AIAnalyzer aIAnalyzer = aIAnalyzerMapper.toEntity(aIAnalyzerDTO);
    //        aIAnalyzer = aIAnalyzerRepository.save(aIAnalyzer);
    //        return aIAnalyzerMapper.toDto(aIAnalyzer);
    //    }

    private void loadModel() throws Exception {
        // تحميل البيانات التدريبية من ملف ARFF
        DataSource source = new DataSource("symptoms_with_vaccine.arff");
        Instances data = source.getDataSet();

        // تحديد العمود الذي يمثل الفئة المستهدفة (التشخيص)
        if (data.classIndex() == -1) data.setClassIndex(data.numAttributes() - 1); // آخر عمود

        this.datasetStructure = new Instances(data, 0); // الهيكل بدون بيانات

        // إنشاء مصنف NaiveBayes وتدريبه على البيانات
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(data);
        this.classifier = nb;
    }

    public String predictDiagnosis(String vaccineName, boolean fever, boolean redness, boolean swelling, boolean rash) throws Exception {
        // إنشاء نسخة جديدة لتمثيل المدخلات
        Instance instance = new DenseInstance(datasetStructure.numAttributes());
        instance.setDataset(datasetStructure);

        // تعبئة القيم للسمات (Attributes)

        instance.setValue(0, "polio"); // 🏥 اسم التطعيم
        instance.setValue(1, fever ? "yes" : "no"); // حرارة
        instance.setValue(2, redness ? "yes" : "no"); // احمرار
        instance.setValue(3, swelling ? "yes" : "no"); // انتفاخ
        instance.setValue(4, rash ? "yes" : "no"); // طفح أو حساسية

        // تصنيف العينة باستخدام النموذج المدرب
        double result = classifier.classifyInstance(instance);

        // استخراج التشخيص النهائي (اسم الفئة)
        return datasetStructure.classAttribute().value((int) result);
    }
    //    /**
    //     * Update a aIAnalyzer.
    //     *
    //     * @param aIAnalyzerDTO the entity to save.
    //     * @return the persisted entity.
    //     */
    //    public AIAnalyzerDTO update(AIAnalyzerDTO aIAnalyzerDTO) {
    //        LOG.debug("Request to update AIAnalyzer : {}", aIAnalyzerDTO);
    //        AIAnalyzer aIAnalyzer = aIAnalyzerMapper.toEntity(aIAnalyzerDTO);
    //        aIAnalyzer = aIAnalyzerRepository.save(aIAnalyzer);
    //        return aIAnalyzerMapper.toDto(aIAnalyzer);
    //    }
    //
    //    /**
    //     * Partially update a aIAnalyzer.
    //     *
    //     * @param aIAnalyzerDTO the entity to update partially.
    //     * @return the persisted entity.
    //     */
    //    public Optional<AIAnalyzerDTO> partialUpdate(AIAnalyzerDTO aIAnalyzerDTO) {
    //        LOG.debug("Request to partially update AIAnalyzer : {}", aIAnalyzerDTO);
    //
    //        return aIAnalyzerRepository
    //            .findById(aIAnalyzerDTO.getId())
    //            .map(existingAIAnalyzer -> {
    //                aIAnalyzerMapper.partialUpdate(existingAIAnalyzer, aIAnalyzerDTO);
    //
    //                return existingAIAnalyzer;
    //            })
    //            .map(aIAnalyzerRepository::save)
    //            .map(aIAnalyzerMapper::toDto);
    //    }
    //
    //    /**
    //     * Get all the aIAnalyzers.
    //     *
    //     * @param pageable the pagination information.
    //     * @return the list of entities.
    //     */
    //    @Transactional(readOnly = true)
    //    public Page<AIAnalyzerDTO> findAll(Pageable pageable) {
    //        LOG.debug("Request to get all AIAnalyzers");
    //        return aIAnalyzerRepository.findAll(pageable).map(aIAnalyzerMapper::toDto);
    //    }

    //    /**
    //     * Get one aIAnalyzer by id.
    //     *
    //     * @param id the id of the entity.
    //     * @return the entity.
    //     */
    //    @Transactional(readOnly = true)
    //    public Optional<AIAnalyzerDTO> findOne(Long id) {
    //        LOG.debug("Request to get AIAnalyzer : {}", id);
    //        return aIAnalyzerRepository.findById(id).map(aIAnalyzerMapper::toDto);
    //    }
    //
    //    /**
    //     * Delete the aIAnalyzer by id.
    //     *
    //     * @param id the id of the entity.
    //     */
    //    public void delete(Long id) {
    //        LOG.debug("Request to delete AIAnalyzer : {}", id);
    //        aIAnalyzerRepository.deleteById(id);
    //    }
}
