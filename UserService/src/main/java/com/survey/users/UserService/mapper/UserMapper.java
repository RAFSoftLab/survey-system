package com.survey.users.UserService.mapper;

import com.survey.users.UserService.domain.Role;
import com.survey.users.UserService.domain.User;
import com.survey.users.UserService.domain.UserHasOrganization;
import com.survey.users.UserService.dto.user.UserCreateDto;
import com.survey.users.UserService.dto.user.UserDto;
import com.survey.users.UserService.dto.user.UserUpdateDto;
import com.survey.users.UserService.exception.NotFoundException;
import com.survey.users.UserService.repository.RoleRepository;
import com.survey.users.UserService.repository.UserHasOrganizationRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    public User userCreateDtoToUser(UserCreateDto userCreateDto){
        User user = new User();
        Role role = roleRepository.findRoleByName(userCreateDto.getRole()).orElseThrow(() -> new NotFoundException(String.format("Role %s not found", userCreateDto.getRole())));
        user.setRole(role);
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());

        return user;
    }

    public UserDto userToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());

        return userDto;
    }

    public User userUpdateDtoToUser(User user, UserUpdateDto userUpdateDto){
        user.setEmail(userUpdateDto.getEmail());
        user.setUsername(userUpdateDto.getUsername());
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());

        return user;
    }


}
