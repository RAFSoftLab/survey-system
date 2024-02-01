package com.survey.users.UserService.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "survey_user", indexes = {@Index(columnList = "username", unique = true), @Index(columnList = "username, password", unique = true), @Index(columnList = "email", unique = true)})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, max = 20, message = "Password length must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).*$", message = "Password must contain letters and numbers")
    private String password;
    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    private String firstName;
    private String lastName;

    @ManyToOne
    private Role role;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean disabled;

}
