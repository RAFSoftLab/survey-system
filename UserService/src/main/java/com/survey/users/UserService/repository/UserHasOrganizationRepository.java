package com.survey.users.UserService.repository;

import com.survey.users.UserService.domain.Organization;
import com.survey.users.UserService.domain.User;
import com.survey.users.UserService.domain.UserHasOrganization;
import com.survey.users.UserService.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserHasOrganizationRepository extends JpaRepository<UserHasOrganization, Long> {

    Optional<UserHasOrganization> findUserHasOrganizationByOrganizationAndUser(Organization organization, User user);

    Page<UserDto> findUserHasOrganizationByOrganization(Organization organization, Pageable pageable);
}
