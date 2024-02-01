package com.survey.users.TargetSurveyService.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.survey.users.TargetSurveyService.domain.Connector;
import com.survey.users.TargetSurveyService.domain.User;
import com.survey.users.TargetSurveyService.dto.RafUsersCsv;
import com.survey.users.TargetSurveyService.dto.ScheduleDto;
import com.survey.users.TargetSurveyService.repository.ConnectionsRepository;
import com.survey.users.TargetSurveyService.repository.ConnectorRepository;
import com.survey.users.TargetSurveyService.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FileLoader {

    private UserRepository userRepository;
    private ConnectorRepository connectorRepository;

    public List<User> loadUserData(String filePath){
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            CsvToBean<RafUsersCsv> csvToBean = new CsvToBeanBuilder<RafUsersCsv>(reader)
                    .withType(RafUsersCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<RafUsersCsv> data = csvToBean.parse();
            List<User> users = new ArrayList<>();
            for(RafUsersCsv rafUser : data){
                User u = new User();
                String indeks[] = rafUser.getIndeks().split("\\s|/");
                String imeIPrezime[] = rafUser.getImeIPrezime().split(" ");
                if(imeIPrezime.length < 2) {System.out.println("Greska: " + rafUser); continue;}
                String username;

                if(imeIPrezime.length > 2) {
                    System.out.println("Resi " + rafUser);
                    continue;
                }
                if(imeIPrezime[0].startsWith("Lj") || imeIPrezime[0].startsWith("Dz") || imeIPrezime[0].startsWith("Dj")) {
                    username = imeIPrezime[0].substring(0,2) + imeIPrezime[1] + indeks[1] + indeks[2] + indeks[0];
                }else
                    username = imeIPrezime[0].charAt(0) + imeIPrezime[1] + indeks[1]+indeks[2]+indeks[0];
                username = replaceSpecialCharacters(username.toLowerCase());
                String email = username+ "@raf.rs";
                u.setUsername(username);
                u.setEmail(email);
                u.setGrupa(rafUser.getGrupa());

                u = userRepository.save(u);
                users.add(u);
            }

            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String replaceSpecialCharacters(String input) {
        // Replace ž with z, š with s, đ with dj, č with c, ć with c
        return input
                .replace("ž", "z")
                .replace("š", "s")
                .replace("đ", "dj")
                .replace("č", "c")
                .replace("ć", "c");
        // Add more replacements as needed
    }

    public List<Connector> loadConnectorData(String filePath){
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            CsvToBean<ScheduleDto> csvToBean = new CsvToBeanBuilder<ScheduleDto>(reader)
                    .withType(ScheduleDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<ScheduleDto> data = csvToBean.parse();
            List<Connector> connectors = new ArrayList<>();
            for(ScheduleDto scheduleDto : data){
                Connector connector = new Connector();
                connector.setName(scheduleDto.getPredmet());
                connector.setType(scheduleDto.getTip());
                connector.setSubject(scheduleDto.getPredmet());
                connector.setPerson(scheduleDto.getNastavnik());
                connector = connectorRepository.save(connector);
                connectors.add(connector);

            }

            return connectors;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
