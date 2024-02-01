package com.survey.users.TargetSurveyService.repository;

import com.survey.users.TargetSurveyService.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}