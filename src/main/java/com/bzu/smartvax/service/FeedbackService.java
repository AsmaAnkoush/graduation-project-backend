package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Feedback;
import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.FeedbackRepository;
import com.bzu.smartvax.repository.ParentRepository;
import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.dto.FeedbackAnalysisResponseDTO;
import com.bzu.smartvax.service.dto.FeedbackDTO;
import com.bzu.smartvax.service.mapper.FeedbackMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bzu.smartvax.domain.Feedback}.
 */
@Service
@Transactional
public class FeedbackService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    //    @Autowired
    private ParentRepository parentRepository;

    //    @Autowired
    private VaccinationRepository vaccinationRepository;

    //    @Autowired
    //    private AIAnalyzerRepository aiAnalyzerRepository; k

    //    @Autowired
    private AIAnalyzerService aiAnalyzerService;

    private GeminiAIService geminiAIService;

    private OpenAIService openAIService;

    private final FeedbackMapper feedbackMapper;

    public FeedbackService(
        FeedbackRepository feedbackRepository,
        FeedbackMapper feedbackMapper,
        ParentRepository parentRepository,
        VaccinationRepository vaccinationRepository,
        AIAnalyzerService aiAnalyzerService,
        GeminiAIService geminiAIService,
        OpenAIService openAIService
    ) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.parentRepository = parentRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.aiAnalyzerService = aiAnalyzerService;
        this.geminiAIService = geminiAIService;
        this.openAIService = openAIService;
    }

    /**
     * Save a feedback.
     *
     * @param feedbackDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to save Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(feedback);
    }

    public FeedbackAnalysisResponseDTO submitFeedback(FeedbackDTO dto) throws Exception {
        //        Parent parent = parentRepository.findById(dto.getParent().getId())
        //            .orElseThrow(() -> new RuntimeException("Parent not found"));

        Vaccination vaccination = vaccinationRepository
            .findById(dto.getVaccination().getId())
            .orElseThrow(() -> new RuntimeException("Vaccination not found"));

        Feedback feedback = new Feedback();
        feedback.setSideEffects(dto.getSideEffects()); // استخدم sideEffects هنا
        //        feedback.setParent(parent);
        feedback.setVaccination(vaccination);
        feedback.setMessageText("just a text");
        //        feedback.setDateSubmitted(LocalDateTime.now());

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return analyzeAndSaveFeedback(savedFeedback);
    }

    private FeedbackAnalysisResponseDTO analyzeAndSaveFeedback(Feedback feedback) throws Exception {
        String symptoms = feedback.getSideEffects();
        String vaccineName = feedback.getVaccination().getName();

        boolean fever = symptoms.contains("حرارة") || symptoms.contains("سخونة");
        boolean redness = symptoms.contains("احمرار");
        boolean swelling = symptoms.contains("انتفاخ");
        boolean rash = symptoms.contains("طفح") || symptoms.contains("حساسية");
        boolean headache = symptoms.contains("صداع");
        boolean vomiting = symptoms.contains("تقيؤ") || symptoms.contains("ترجيع");
        boolean fatigue = symptoms.contains("تعب");
        boolean lossOfAppetite = symptoms.contains("فقدان شهية") || symptoms.contains("قلة أكل");

        String diagnosis = aiAnalyzerService.predictDiagnosis(
            vaccineName,
            fever,
            redness,
            swelling,
            rash,
            headache,
            vomiting,
            fatigue,
            lossOfAppetite
        );

        String suggestions = geminiAIService.getSuggestions(symptoms);

        return new FeedbackAnalysisResponseDTO(diagnosis, suggestions);
    }

    /**
     * Update a feedback.
     *
     * @param feedbackDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedbackDTO update(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to update Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(feedback);
    }

    /**
     * Partially update a feedback.
     *
     * @param feedbackDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to partially update Feedback : {}", feedbackDTO);

        return feedbackRepository
            .findById(feedbackDTO.getId())
            .map(existingFeedback -> {
                feedbackMapper.partialUpdate(existingFeedback, feedbackDTO);

                return existingFeedback;
            })
            .map(feedbackRepository::save)
            .map(feedbackMapper::toDto);
    }

    /**
     * Get all the feedbacks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FeedbackDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Feedbacks");
        return feedbackRepository.findAll(pageable).map(feedbackMapper::toDto);
    }

    /**
     * Get one feedback by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FeedbackDTO> findOne(Long id) {
        LOG.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id).map(feedbackMapper::toDto);
    }

    /**
     * Delete the feedback by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Feedback : {}", id);
        feedbackRepository.deleteById(id);
    }
}
