package com.goev.auth.service.auth.impl;

import com.goev.auth.dao.OrganizationDao;
import com.goev.auth.dao.auth.AuthClientDao;
import com.goev.auth.dto.auth.AuthClientDto;
import com.goev.auth.repository.organization.OrganizationRepository;
import com.goev.auth.service.auth.AuthClientService;
import com.goev.auth.utilities.RequestContext;
import com.goev.lib.exceptions.ResponseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthClientServiceImpl implements AuthClientService {
    private final OrganizationRepository organizationRepository;
    @Override
    public AuthClientDto getClientDetails() {

        AuthClientDao clientDao = RequestContext.getClient();
        if (clientDao == null)
            throw new ResponseException("Invalid Client");
        OrganizationDao organizationDao = organizationRepository.findById(clientDao.getOrganizationId());

        return AuthClientDto.builder()
                .uuid(clientDao.getUuid())
                .clientKey(clientDao.getClientKey())
                .isUserRegistrationAllowed(clientDao.getIsUserRegistrationAllowed())
                .organizationUUID(organizationDao.getUuid())
                .build();
    }
}
