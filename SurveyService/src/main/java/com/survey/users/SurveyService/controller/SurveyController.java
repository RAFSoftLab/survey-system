package com.survey.users.SurveyService.controller;

import com.survey.users.SurveyService.dto.*;
import com.survey.users.SurveyService.service.SurveyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/survey")
@AllArgsConstructor
@CrossOrigin
public class SurveyController {

    private SurveyService surveyService;

    @GetMapping("/organization/{id}/all")
    public ResponseEntity<Page<SurveyDto>> getAllForOrganization(@PathVariable Long id, Pageable pageable){
        return new ResponseEntity<>(surveyService.getSurveyForOrganization(id, pageable), HttpStatus.OK);
    }

    @GetMapping("/organization/{id}/my/{idx}")
    public ResponseEntity<Page<SurveyDto>> getMineForOrganization(@PathVariable Long id,@PathVariable Long idx, Pageable pageable){
        return new ResponseEntity<>(surveyService.getMySurveyForOrganization(id, idx, pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}/detailed")
    public ResponseEntity<SurveyDetailedDto> getSurveyDetailed(@PathVariable Long id){
        return new ResponseEntity<>(surveyService.getSurveyDetailed(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable Long id){
        return new ResponseEntity<>(surveyService.getSurvey(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SurveyDto> addSurvey(@RequestBody SurveyCreateDto surveyCreateDto){
        return new ResponseEntity<>(surveyService.createSurvey(surveyCreateDto), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SurveyDto> updateSurvey(@RequestBody SurveyUpdateDto surveyUpdateDto){
        return new ResponseEntity<>(surveyService.updateSurveyData(surveyUpdateDto), HttpStatus.OK);
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionForSurveyDto>> getSurveyQuestions(@PathVariable Long id){
        return new ResponseEntity<>(surveyService.getQuestions(id), HttpStatus.OK);
    }

    @PostMapping("/question")
    public ResponseEntity<MessageDto> addQuestionForSurvey(@RequestBody QuestionForSurveyDto questionForSurveyDto){
        return new ResponseEntity<>(surveyService.addQuestionsForSurvey(questionForSurveyDto), HttpStatus.OK);
    }

    @PutMapping("/question")
    public ResponseEntity<MessageDto> updateQuestionForSurvey(@RequestBody QuestionForSurveyDto questionForSurveyDto){
        return new ResponseEntity<>(surveyService.updateQuestionsForSurvey(questionForSurveyDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteQuestion(@PathVariable Long id){
        return new ResponseEntity<>(surveyService.deleteQuestionForSurvey(id), HttpStatus.OK);
    }


}
