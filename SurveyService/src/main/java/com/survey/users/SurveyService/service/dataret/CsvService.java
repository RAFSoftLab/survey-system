package com.survey.users.SurveyService.service.dataret;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.survey.users.SurveyService.domain.*;
import com.survey.users.SurveyService.dto.dataret.RafCsvData;
import com.survey.users.SurveyService.exception.NotFoundException;
import com.survey.users.SurveyService.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CsvService {

    private DomainRepository domainRepository;
    private NodeRepository nodeRepository;
    private SurveyRepository surveyRepository;
    private ChoiceRepository choiceRepository;
    private QuestionRepository questionRepository;
    private SurveyHasQuestionRepository surveyHasQuestionRepository;

    public List<RafCsvData> readCsvFile(String filePath, String domainName, Long idNodeParent) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            CsvToBean<RafCsvData> csvToBean = new CsvToBeanBuilder<RafCsvData>(reader)
                    .withType(RafCsvData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Long organizationId = 1L;
            Long userId = 1L;

            List<RafCsvData> data = csvToBean.parse();
            Domain domain = domainRepository.findByName(domainName).orElseThrow(()->new NotFoundException(String.format("Domain with name %s not found", domainName)));
            String type = data.get(0).getTip();

            Node parent = nodeRepository.findById(idNodeParent).get();
            Node node = new Node();
            node.setName(type.toUpperCase());
            node.setDomain(domain);
            node.setParent(parent);

            Survey survey = new Survey();
            survey.setOrganizationId(organizationId);
            survey.setUserId(userId);
            survey.setDescription(type.toUpperCase());
            survey.setTitle(type.toUpperCase());
            survey = surveyRepository.save(survey);
            node.setSurvey(survey);
            node.setOrganizationId(organizationId);
            node.setUserId(userId);
            node = nodeRepository.save(node);

            int qNum = 1;
            for(RafCsvData d : data){
                if(!d.getTip().equals(type)){
                    type = d.getTip();
                    node = new Node();
                    node.setName(type.toUpperCase());
                    node.setDomain(domain);

                    survey = new Survey();
                    survey.setDescription(type.toUpperCase());
                    survey.setTitle(type.toUpperCase());
                    survey.setOrganizationId(organizationId);
                    survey.setUserId(userId);
                    survey = surveyRepository.save(survey);
                    node.setSurvey(survey);
                    node.setOrganizationId(organizationId);
                    node.setUserId(userId);
                    node.setParent(parent);
                    node = nodeRepository.save(node);

                    qNum = 1;
                }
                Question q;
                if(d.getFormat().equals("ocena")){
                    q = new GradeQuestion();
                    q.setText(d.getTekst());
                    q = questionRepository.save(q);
                }
                else if(d.getFormat().equals("tekst")){
                    q = new OpenEndedQuestion();
                    q.setText(d.getTekst());
                    q = questionRepository.save(q);
                }
                else {
                    q = new MultipleChoiceQuestion();
                    q.setText(d.getTekst());
                    q = questionRepository.save(q);
                    String choices[] = d.getFormat().split("\\|");
                    for(int i = 0; i<choices.length; i++){
                        Choice choice = new Choice();
                        choice.setText(choices[i]);
                        choice.setNum(i+1);
                        choice.setMultipleChoiceQuestion((MultipleChoiceQuestion) q);
                        choice = choiceRepository.save(choice);
                    }
                }

                SurveyHasQuestion surveyHasQuestion = new SurveyHasQuestion();
                surveyHasQuestion.setQuestion(q);
                surveyHasQuestion.setSurvey(survey);
                surveyHasQuestion.setNumber(qNum);
                surveyHasQuestionRepository.save(surveyHasQuestion);

                qNum++;

            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
