package com.survey.users.UserService.runner;

import com.survey.users.UserService.domain.Address;
import com.survey.users.UserService.domain.Organization;
import com.survey.users.UserService.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppRunner implements CommandLineRunner {

    private OrganizationRepository organizationRepository;

    @Override
    public void run(String... args) throws Exception {
        Address address = new Address();
        address.setCountry("Serbia");
        address.setCity("Belgrade");
        address.setStreet("Kneza Mihaila");
        address.setNumber("6");
        address.setPostcode("11000");

        Organization organization = new Organization();
        organization.setActive(true);
        organization.setName("Racunarski Fakultet - RAF");
        organization.setWebpage("https://raf.edu.rs");
        organization.setPhoneNumber("112233");
        organization.setAddress(address);
        organization.setAbout("An educational institution with a 20-year long tradition in teaching programming to young people");

        organizationRepository.save(organization);
    }
}
