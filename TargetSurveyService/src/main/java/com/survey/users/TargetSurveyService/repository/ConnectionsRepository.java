package com.survey.users.TargetSurveyService.repository;

import com.survey.users.TargetSurveyService.domain.Connections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connections, Long> {

}
