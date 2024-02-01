package com.survey.users.SurveyService.service.impl;

import com.survey.users.SurveyService.domain.*;
import com.survey.users.SurveyService.dto.*;
import com.survey.users.SurveyService.exception.NotFoundException;
import com.survey.users.SurveyService.mapper.NodeMapper;
import com.survey.users.SurveyService.mapper.QuestionMapper;
import com.survey.users.SurveyService.mapper.SurveyMapper;
import com.survey.users.SurveyService.repository.NodeRepository;
import com.survey.users.SurveyService.repository.QuestionRepository;
import com.survey.users.SurveyService.repository.SurveyHasQuestionRepository;
import com.survey.users.SurveyService.repository.SurveyRepository;
import com.survey.users.SurveyService.service.SurveyService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private SurveyRepository surveyRepository;
    private QuestionRepository questionRepository;
    private SurveyHasQuestionRepository surveyHasQuestionRepository;
    private SurveyMapper surveyMapper;
    private QuestionMapper questionMapper;

    @Override
    public Page<SurveyDto> getSurveyForOrganization(Long organization, Pageable pageable) {
        return surveyRepository.findByOrganizationId(organization, pageable)
                .map(surveyMapper::surveyToSurveyDto);
    }

    @Override
    public SurveyDto getSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).get();
        return surveyMapper.surveyToSurveyDto(survey);
    }

    @Override
    public Page<SurveyDto> getMySurveyForOrganization(Long id, Long idx, Pageable pageable) {
        return surveyRepository.findByOrganizationIdAndUserId(id, idx, pageable)
                .map(surveyMapper::surveyToSurveyDto);
    }

    @Override
    public SurveyDetailedDto getSurveyDetailed(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(()->new NotFoundException("Survey not found"));

        return surveyMapper.surveyToDetailedSurvey(survey);
    }

    @Override
    public List<QuestionForSurveyDto> getQuestions(Long id) {
        SurveyDetailedDto dto = getSurveyDetailed(id);
        return dto.getQuestionForSurveyDto();
    }

    @Override
    public SurveyDto createSurvey(SurveyCreateDto surveyCreateDto) {
        Survey survey = surveyMapper.surveyCreateDtoToSurvey(surveyCreateDto);

        survey = surveyRepository.save(survey);
        return surveyMapper.surveyToSurveyDto(survey);
    }

    @Override
    public SurveyDto updateSurveyData(SurveyUpdateDto surveyUpdateDto) {
        Survey survey = surveyRepository.findById(surveyUpdateDto.getId()).orElseThrow(() -> new RuntimeException("Survey not found"));
        Long id = survey.getId();
        survey = surveyMapper.surveyCreateDtoToSurvey(surveyUpdateDto);
        survey.setId(id);
        survey = surveyRepository.save(survey);
        return surveyMapper.surveyToSurveyDto(survey);
    }

    @Override
    public MessageDto addQuestionsForSurvey(QuestionForSurveyDto questionsForSurveyDto) {
        Question question = questionMapper.questionCreateDtoToQuestion(questionsForSurveyDto);
        question = questionRepository.save(question);
        SurveyHasQuestion surveyHasQuestion = new SurveyHasQuestion();
        surveyHasQuestion.setQuestion(question);
        Survey survey = surveyRepository.findById(questionsForSurveyDto.getSurveyId()).orElseThrow(()->new NotFoundException("Survey not found"));
        surveyHasQuestion.setSurvey(survey);
        surveyHasQuestion.setNumber(questionsForSurveyDto.getNumber());

        surveyHasQuestionRepository.save(surveyHasQuestion);

        return new MessageDto("Question successfully added");
    }

    @Override
    public MessageDto updateQuestionsForSurvey(QuestionForSurveyDto questionForSurveyDto) {
        Survey survey = surveyRepository.findById(questionForSurveyDto.getSurveyId()).orElseThrow(()->new NotFoundException("Survey not found"));
        /// SurveyHasQuestion surveyHasQuestion = surveyHasQuestionRepository.findQuestionBySurveyAndNumber(survey, questionForSurveyDto.getNumber());

        return new MessageDto("survey successfully updated");
    }

    @Override
    public MessageDto deleteQuestionForSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));

        surveyRepository.delete(survey);
        return new MessageDto("Survey successfully deleted");
    }

}
