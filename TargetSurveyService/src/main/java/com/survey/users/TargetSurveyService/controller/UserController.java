package com.survey.users.TargetSurveyService.controller;

import com.survey.users.TargetSurveyService.domain.Connector;
import com.survey.users.TargetSurveyService.domain.User;
import com.survey.users.TargetSurveyService.service.FileLoader;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/target-user")
@AllArgsConstructor
public class UserController {

    private FileLoader fileLoader;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(fileLoader.loadUserData("C:\\Users\\mijan\\OneDrive\\Desktop\\master\\TargetSurveyService\\TargetSurveyService\\src\\main\\resources\\static\\studenti.csv"), HttpStatus.OK);
    }

    @GetMapping("/connectors")
    public ResponseEntity<List<Connector>> getConnectos(){
        return new ResponseEntity<>(fileLoader.loadConnectorData("C:\\Users\\mijan\\OneDrive\\Desktop\\master\\TargetSurveyService\\TargetSurveyService\\src\\main\\resources\\static\\schedule.csv"), HttpStatus.OK);
    }
}
