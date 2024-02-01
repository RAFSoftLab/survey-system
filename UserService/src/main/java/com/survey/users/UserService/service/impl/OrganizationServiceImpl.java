package com.survey.users.UserService.service.impl;

import com.survey.users.UserService.domain.Organization;
import com.survey.users.UserService.dto.message.MessageDto;
import com.survey.users.UserService.dto.organization.*;
import com.survey.users.UserService.dto.user.UserDto;
import com.survey.users.UserService.exception.NotFoundException;
import com.survey.users.UserService.mapper.OrganizationMapper;
import com.survey.users.UserService.repository.OrganizationRepository;
import com.survey.users.UserService.repository.UserHasOrganizationRepository;
import com.survey.users.UserService.service.OrganizationService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository organizationRepository;
    private UserHasOrganizationRepository userHasOrganizationRepository;
    private OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   UserHasOrganizationRepository userHasOrganizationRepository,
                                   OrganizationMapper organizationMapper){
        this.organizationRepository = organizationRepository;
        this.userHasOrganizationRepository = userHasOrganizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public Page<OrganizationDto> getAllOrg(Pageable pageable) {
        return organizationRepository.findAll(pageable)
                .map(organizationMapper::organizationToOrganizationDto);
    }

    @Override
    public Page<OrganizationDto> getAllOrgFiltered(OrganizationFilterDto organizationFilterDto, Pageable pageable) {
        return organizationRepository.findAllFiltered(organizationFilterDto.getName(), organizationFilterDto.getCity(), organizationFilterDto.getCountry(), organizationFilterDto.isActive(), pageable)
                .map(organizationMapper::organizationToOrganizationDto);
    }

    @Override
    public Page<UserDto> getUsersForOrganization(Long id, Pageable pageable) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new NotFoundException("Organization not found"));
        return userHasOrganizationRepository.findUserHasOrganizationByOrganization(organization, pageable);
    }

    @Override
    public OrganizationDto addNewOrganization(OrganizationCreateDto organizationCreateDto) {
        Organization organization = organizationMapper.organizationCreateDtoToOrganization(organizationCreateDto);
        organization = organizationRepository.save(organization);
        return organizationMapper.organizationToOrganizationDto(organization);
    }

    @Override
    public OrganizationDto updateOrganization(OrganizationUpdateDto organizationUpdateDto) {
        Organization organization = organizationRepository.findById(organizationUpdateDto.getId()).orElseThrow(()->new NotFoundException("Organization not found"));
        organization = organizationMapper.organizationUpdateDtoToOrganization(organization, organizationUpdateDto);
        organization = organizationRepository.save(organization);
        return organizationMapper.organizationToOrganizationDto(organization);
    }

    @Override
    public OrganizationDetailedDto getOrganizationDetailed(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(()->new NotFoundException("Organization not found"));
        return organizationMapper.organizationToOrganizationDetailedDto(organization);
    }

    @Override
    public MessageDto deactivateOrganization(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(()->new NotFoundException("Organization not found"));
        organization.setActive(false);
        return new MessageDto("Organization successfully deactivated");
    }
}
