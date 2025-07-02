package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Feedback;
import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.FeedbackRepository;
import com.bzu.smartvax.repository.ParentRepository;
import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.dto.FeedbackAnalysisResponseDTO;
import com.bzu.smartvax.service.dto.FeedbackDTO;
import com.bzu.smartvax.service.mapper.FeedbackMapper;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class FeedbackService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;
    private final ParentRepository parentRepository;
    private final VaccinationRepository vaccinationRepository;
    private final AIAnalyzerService aiAnalyzerService;
    private final GeminiAIService geminiAIService;
    private final OpenAIService openAIService;
    private final FeedbackMapper feedbackMapper;
    private final GeminiVaccineValidationService geminiVaccineValidationService;

    public FeedbackService(
        FeedbackRepository feedbackRepository,
        FeedbackMapper feedbackMapper,
        ParentRepository parentRepository,
        VaccinationRepository vaccinationRepository,
        AIAnalyzerService aiAnalyzerService,
        GeminiAIService geminiAIService,
        OpenAIService openAIService,
        GeminiVaccineValidationService geminiVaccineValidationService
    ) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.parentRepository = parentRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.aiAnalyzerService = aiAnalyzerService;
        this.geminiAIService = geminiAIService;
        this.openAIService = openAIService;
        this.geminiVaccineValidationService = geminiVaccineValidationService;
    }

    // ... باقي الدوال ...

    public FeedbackAnalysisResponseDTO submitFeedback(FeedbackDTO dto) throws Exception {
        Vaccination vaccination = vaccinationRepository
            .findById(dto.getVaccination().getId())
            .orElseThrow(() -> new RuntimeException("Vaccination not found"));

        // تحقق من أن النص المدخل متعلق بالتطعيمات أو الأعراض بعدها
        String symptoms = dto.getSideEffects();
        boolean isRelated = geminiVaccineValidationService.isVaccineRelated(symptoms);
        if (!isRelated) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "❌ يرجى كتابة نص متعلق بالتطعيمات أو أعراض ما بعد التطعيم فقط.");
        }

        // تحليل الأعراض باستخدام NLP
        String arffVaccineName = convertVaccineNameToArffFormat(vaccination.getName());
        Map<String, String> symptomsMap = SymptomNLPProcessor.processSymptoms(symptoms);

        String diagnosis = aiAnalyzerService.predictDiagnosis(arffVaccineName, symptomsMap);

        // إنشاء Feedback جديد أو تحديثه مع الأعراض والتشخيص فقط
        Feedback feedback = new Feedback();
        feedback.setSideEffects(dto.getSideEffects());
        feedback.setVaccination(vaccination);
        feedback.setMessageText(diagnosis);
        feedback.setDateSubmitted(Instant.now());
        feedback.setParent(
            parentRepository.findById(dto.getParent().getId())
                .orElseThrow(() -> new RuntimeException("Parent not found"))
        );

        Feedback savedFeedback = feedbackRepository.save(feedback);

        String suggestions = geminiAIService.getSuggestions(symptoms);

        return new FeedbackAnalysisResponseDTO(diagnosis, suggestions, savedFeedback.getId());
    }

    // باقي دوال FeedbackService كما هي...

    private String convertVaccineNameToArffFormat(String name) {
        return name.replace(" ", "_").replace("(", "").replace(")", "");
    }

    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to save or update Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);

        if (feedback.getId() != null) {
            Optional<Feedback> existingFeedbackOpt = feedbackRepository.findById(feedback.getId());
            if (existingFeedbackOpt.isPresent()) {
                Feedback existingFeedback = existingFeedbackOpt.get();
                if (feedback.getSideEffects() != null) {
                    existingFeedback.setSideEffects(feedback.getSideEffects());
                }
                if (feedback.getMessageText() != null) {
                    existingFeedback.setMessageText(feedback.getMessageText());
                }
                if (feedback.getTreatment() != null) {
                    existingFeedback.setTreatment(feedback.getTreatment());
                }
                if (feedback.getDateSubmitted() != null) {
                    existingFeedback.setDateSubmitted(feedback.getDateSubmitted());
                }
                if (feedback.getVaccination() != null && feedback.getVaccination().getId() != null) {
                    existingFeedback.setVaccination(vaccinationRepository.findById(feedback.getVaccination().getId()).orElse(null));
                }
                if (feedback.getParent() != null && feedback.getParent().getId() != null) {
                    existingFeedback.setParent(parentRepository.findById(feedback.getParent().getId()).orElse(null));
                }
                feedback = existingFeedback;
            }
        } else {
            feedback.setDateSubmitted(Instant.now());
        }

        if (feedback.getVaccination() != null && feedback.getVaccination().getId() != null) {
            Vaccination vaccination = vaccinationRepository
                .findById(feedback.getVaccination().getId())
                .orElseThrow(() -> new RuntimeException("Vaccination not found"));
            feedback.setVaccination(vaccination);
        }
        if (feedback.getParent() != null && feedback.getParent().getId() != null) {
            feedback.setParent(parentRepository.findById(feedback.getParent().getId()).orElse(null));
        }

        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(feedback);
    }

    public Optional<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to partially update Feedback : {}", feedbackDTO);

        return feedbackRepository
            .findById(feedbackDTO.getId())
            .map(existingFeedback -> {
                if (feedbackDTO.getSideEffects() != null) {
                    existingFeedback.setSideEffects(feedbackDTO.getSideEffects());
                }
                if (feedbackDTO.getMessageText() != null) {
                    existingFeedback.setMessageText(feedbackDTO.getMessageText());
                }
                if (feedbackDTO.getTreatment() != null) {
                    existingFeedback.setTreatment(feedbackDTO.getTreatment());
                }
                if (feedbackDTO.getDateSubmitted() != null) {
                    existingFeedback.setDateSubmitted(feedbackDTO.getDateSubmitted());
                }
                return existingFeedback;
            })
            .map(feedbackRepository::save)
            .map(feedbackMapper::toDto);
    }

    public Page<FeedbackDTO> findAllByVaccinationId(Long vaccineId, Pageable pageable) {
        LOG.debug("Request to get all Feedbacks for Vaccination ID: {}", vaccineId);
        return feedbackRepository.findByVaccinationId(vaccineId, pageable).map(feedbackMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<FeedbackDTO> findOne(Long id) {
        LOG.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id).map(feedbackMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Feedback : {}", id);
        feedbackRepository.deleteById(id);
    }
}
