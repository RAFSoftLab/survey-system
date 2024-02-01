package com.survey.users.SurveyService.controller;

import com.survey.users.SurveyService.dto.SurveyCreateDto;
import com.survey.users.SurveyService.dto.SurveyDto;
import com.survey.users.SurveyService.dto.dataret.DataPath;
import com.survey.users.SurveyService.dto.dataret.RafCsvData;
import com.survey.users.SurveyService.service.dataret.CsvService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dataret")
@AllArgsConstructor
public class DataRetController {

    private CsvService service;

    @PostMapping
    public ResponseEntity<List<RafCsvData>> getData(@RequestBody DataPath dataPathDto){
        return new ResponseEntity<>(service.readCsvFile(dataPathDto.getPath(), dataPathDto.getDomain(),1L), HttpStatus.OK);
    }

}
