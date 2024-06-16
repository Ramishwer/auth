package com.goev.auth.service.auth.impl;

import com.goev.auth.dao.OrganizationDao;
import com.goev.auth.dao.client.AuthClientCredentialTypeMappingDao;
import com.goev.auth.dao.client.AuthClientDao;
import com.goev.auth.dao.client.AuthCredentialTypeDao;
import com.goev.auth.dao.user.AuthUserCredentialDao;
import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.dto.user.AuthUserDto;
import com.goev.auth.repository.auth.AuthClientCredentialTypeMappingRepository;
import com.goev.auth.repository.auth.AuthClientRepository;
import com.goev.auth.repository.auth.AuthCredentialTypeRepository;
import com.goev.auth.repository.organization.OrganizationRepository;
import com.goev.auth.repository.user.AuthUserCredentialRepository;
import com.goev.auth.repository.user.AuthUserRepository;
import com.goev.auth.service.auth.AuthUserService;
import com.goev.auth.service.keycloak.KeycloakService;
import com.goev.auth.utilities.RequestContext;
import com.goev.auth.utilities.SecretGenerationUtils;
import com.goev.lib.exceptions.ResponseException;
import com.goev.lib.utilities.Md5Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final OrganizationRepository organizationRepository;
    private final AuthClientCredentialTypeMappingRepository authClientCredentialTypeMappingRepository;
    private final AuthCredentialTypeRepository authCredentialTypeRepository;
    private final AuthUserRepository authUserRepository;
    private final KeycloakService keycloakService;
    private final AuthUserCredentialRepository authUserCredentialRepository;
    private final AuthClientRepository authClientRepository;


    @Override
    public AuthUserDto saveUser(AuthUserDto user) {

        AuthClientDao clientDao = RequestContext.getClient();
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        OrganizationDao organizationDao = organizationRepository.findById(clientDao.getOrganizationId());

        if (organizationDao == null)
            throw new ResponseException("Invalid Client");

        AuthClientDao requestClient = authClientRepository.findByUUID(user.getClientUUID());
        if (requestClient == null)
            throw new ResponseException("Invalid Client");

        List<AuthClientCredentialTypeMappingDao> credentialTypeMappingDao = authClientCredentialTypeMappingRepository.findByClientId(requestClient.getId());

        if (CollectionUtils.isEmpty(credentialTypeMappingDao))
            throw new ResponseException("Invalid client details");

        if (user.getPhoneNumber() == null)
            throw new ResponseException("No phone number present");

        AuthUserDao authUserDao = authUserRepository.findByPhoneNumber(user.getPhoneNumber());

        if (authUserDao == null) {
            authUserDao = new AuthUserDao();
            authUserDao.setPhoneNumber(user.getPhoneNumber());
            authUserDao.setEmail(user.getEmail());
            authUserDao.setOrganizationId(organizationDao.getId());
            authUserDao = authUserRepository.save(authUserDao);
        }

        for (AuthClientCredentialTypeMappingDao mappingDao : credentialTypeMappingDao) {

            AuthCredentialTypeDao credentialTypeDao = authCredentialTypeRepository.findById(mappingDao.getAuthCredentialTypeId());
            if (credentialTypeDao == null)
                continue;


            AuthUserCredentialDao credentialDao = authUserCredentialRepository.findByAuthUserIdAndCredentialTypeId(authUserDao.getId(), credentialTypeDao.getId());
            if (credentialDao == null)
                createCredentials(credentialTypeDao, authUserDao, organizationDao, requestClient);

        }

        return AuthUserDto.builder().email(authUserDao.getEmail()).phoneNumber(authUserDao.getPhoneNumber())
                .organizationUUID(organizationDao.getUuid()).uuid(authUserDao.getUuid()).build();
    }

    private void createCredentials(AuthCredentialTypeDao credentialTypeDao, AuthUserDao authUserDao, OrganizationDao organizationDao, AuthClientDao clientDao) {
        String authKey = UUID.randomUUID().toString();
        String authSecret = SecretGenerationUtils.getSecret(11);


        AuthUserCredentialDao credential = new AuthUserCredentialDao();
        credential.setAuthUserId(authUserDao.getId());
        credential.setOrganizationId(organizationDao.getId());
        credential.setAuthKey(authKey);
        credential.setUuid(authKey);
        credential.setAuthSecret(Md5Utils.getMd5(authSecret));
        credential.setAuthCredentialTypeId(credentialTypeDao.getId());
        credential.setAuthClientId(clientDao.getId());

        String keycloakId = keycloakService.addUser(credential, clientDao, authUserDao.getEmail());

        if (keycloakId != null) {
            credential.setKeycloakUuid(keycloakId);
            authUserCredentialRepository.save(credential);
        }
    }
}
