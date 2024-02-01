package com.survey.users.TargetSurveyService.repository;

import com.survey.users.TargetSurveyService.domain.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
}
