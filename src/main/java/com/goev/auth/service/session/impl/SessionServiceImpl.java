package com.goev.auth.service.session.impl;


import com.goev.auth.constant.ApplicationConstants;
import com.goev.auth.dao.OrganizationDao;
import com.goev.auth.dao.client.AuthClientCredentialTypeMappingDao;
import com.goev.auth.dao.client.AuthClientDao;
import com.goev.auth.dao.client.AuthCredentialTypeDao;
import com.goev.auth.dao.user.AuthUserCredentialDao;
import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.dao.user.AuthUserSessionDao;
import com.goev.auth.dto.client.AuthCredentialDto;
import com.goev.auth.dto.client.AuthCredentialTypeDto;
import com.goev.auth.dto.keycloak.KeycloakTokenDto;
import com.goev.auth.dto.keycloak.KeycloakUserDetailsDto;
import com.goev.auth.dto.session.ExchangeTokenRequestDto;
import com.goev.auth.dto.session.SessionDetailsDto;
import com.goev.auth.dto.session.SessionDto;
import com.goev.auth.enums.ResendType;
import com.goev.auth.repository.auth.AuthClientCredentialTypeMappingRepository;
import com.goev.auth.repository.auth.AuthClientRepository;
import com.goev.auth.repository.auth.AuthCredentialTypeRepository;
import com.goev.auth.repository.organization.OrganizationRepository;
import com.goev.auth.repository.user.AuthUserCredentialRepository;
import com.goev.auth.repository.user.AuthUserRepository;
import com.goev.auth.repository.user.AuthUserSessionRepository;
import com.goev.auth.service.keycloak.KeycloakService;
import com.goev.auth.service.session.SessionService;
import com.goev.auth.utilities.MessageUtils;
import com.goev.auth.utilities.RequestContext;
import com.goev.auth.utilities.SecretGenerationUtils;
import com.goev.lib.exceptions.ResponseException;
import com.goev.lib.utilities.Md5Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final KeycloakService keycloakService;
    private final AuthUserRepository authUserRepository;
    private final AuthUserCredentialRepository authUserCredentialRepository;
    private final AuthUserSessionRepository authUserSessionRepository;
    private final AuthClientRepository authClientRepository;
    private final AuthCredentialTypeRepository authCredentialTypeRepository;
    private final AuthClientCredentialTypeMappingRepository authClientCredentialTypeMappingRepository;
    private final OrganizationRepository organizationRepository;
    private final MessageUtils messageUtils;

    @Override
    public SessionDto createSession(AuthCredentialDto credentials) {

        AuthClientDao clientDao = RequestContext.getClient();
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        if (credentials.getAuthCredentialType().getUuid() == null)
            throw new ResponseException("Invalid Credentials");
        AuthCredentialTypeDao credentialTypeDao = authCredentialTypeRepository.findByUUID(credentials.getAuthCredentialType().getUuid());

        if (credentialTypeDao == null)
            throw new ResponseException("Invalid Credentials");

        AuthUserDao user = authUserRepository.findByUUID(credentials.getAuthUUID());

        if (user == null)
            throw new ResponseException("Invalid Credentials");

        AuthUserCredentialDao credentialDao = authUserCredentialRepository.findByAuthUserIdAndCredentialTypeId(user.getId(), credentialTypeDao.getId());
        if (credentialDao == null)
            throw new ResponseException("Invalid Credentials");

        credentials.setAuthKey(credentialDao.getAuthKey());
        credentials.setAuthSecret(Md5Utils.getMd5(credentials.getAuthSecret()));

        KeycloakTokenDto token = keycloakService.login(credentials, clientDao);

        if (token == null)
            throw new ResponseException("Invalid Credentials");


        AuthUserSessionDao session = new AuthUserSessionDao();

        session.setAccessToken(token.getAccessToken());
        session.setRefreshToken(token.getRefreshToken());
        session.setAuthUserCredentialId(credentialDao.getId());
        session.setAuthUserId(credentialDao.getAuthUserId());
        session.setExpiresIn(token.getExpiresIn());
        session.setRefreshExpiresIn(token.getRefreshExpiresIn());
        session.setIdToken(token.getIdToken());
        session = authUserSessionRepository.save(session);
        OrganizationDao organizationDao = organizationRepository.findById(clientDao.getOrganizationId());

        return SessionDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .authUUID(user.getUuid())
                .expiresIn(session.getExpiresIn())
                .refreshExpiresIn(session.getRefreshExpiresIn())
                .uuid(session.getUuid())
                .organizationUUID(organizationDao.getUuid())
                .build();
    }

    @Override
    public SessionDto createSession(ExchangeTokenRequestDto exchangeTokenRequest) {
        AuthClientDao clientDao = RequestContext.getClient();
        if (clientDao == null)
            throw new ResponseException("Invalid Client");


        KeycloakUserDetailsDto userDetailsDto = keycloakService.getUserDetailsForToken("Bearer " + exchangeTokenRequest.getAccessToken(), clientDao);
        if (userDetailsDto == null)
            throw new ResponseException("Invalid Access Token");
        AuthUserCredentialDao userCredentialDao = authUserCredentialRepository.findByKeycloakId(userDetailsDto.getSub());
        if (userCredentialDao == null)
            throw new ResponseException("Invalid Credential");

        AuthUserDao user = authUserRepository.findById(userCredentialDao.getAuthUserId());

        KeycloakTokenDto token = keycloakService.getExchangeTokenForToken(exchangeTokenRequest, clientDao);
        if (token == null)
            throw new ResponseException("Invalid Credential");

        AuthUserSessionDao session = new AuthUserSessionDao();

        session.setAccessToken(token.getAccessToken());
        session.setRefreshToken(token.getRefreshToken());
        session.setAuthUserCredentialId(userCredentialDao.getId());
        session.setAuthUserId(user.getId());
        session.setExpiresIn(token.getExpiresIn());
        session.setRefreshExpiresIn(token.getRefreshExpiresIn());
        session.setIdToken(token.getIdToken());
        session = authUserSessionRepository.save(session);
        OrganizationDao organizationDao = organizationRepository.findById(clientDao.getOrganizationId());

        return SessionDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .authUUID(user.getUuid())
                .expiresIn(session.getExpiresIn())
                .refreshExpiresIn(session.getRefreshExpiresIn())
                .uuid(session.getUuid())
                .organizationUUID(organizationDao.getUuid())
                .build();
    }

    @Override
    public SessionDto refreshSessionForSessionUUID(String sessionUUID) {

        AuthClientDao clientDao = RequestContext.getClient();
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        KeycloakTokenDto token = keycloakService.getTokenForRefreshToken(RequestContext.getRefreshToken(), clientDao);
        if (token == null)
            throw new ResponseException("Invalid Access Token");

        AuthUserSessionDao session = authUserSessionRepository.findByUUID(sessionUUID);
        if (session == null)
            throw new ResponseException("Session Expired");
        AuthUserSessionDao newSession = new AuthUserSessionDao();
        newSession.setAuthUserCredentialId(session.getAuthUserCredentialId());
        newSession.setRefreshToken(token.getAccessToken());
        newSession.setAccessToken(token.getRefreshToken());
        newSession.setExpiresIn(token.getExpiresIn());
        newSession.setRefreshExpiresIn(token.getRefreshExpiresIn());
        newSession.setAuthUserId(session.getAuthUserId());
        newSession.setIdToken(token.getIdToken());
        newSession = authUserSessionRepository.save(newSession);
        AuthUserDao user = authUserRepository.findById(session.getAuthUserId());

        if (user == null)
            throw new ResponseException("Invalid Credentials");
        OrganizationDao organizationDao = organizationRepository.findById(clientDao.getOrganizationId());

        return SessionDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .uuid(newSession.getUuid())
                .authUUID(user.getUuid())
                .organizationUUID(organizationDao.getUuid())
                .build();
    }

    @Override
    public SessionDetailsDto getSessionDetails(String sessionUUID) {
        AuthUserSessionDao session = authUserSessionRepository.findByUUID(sessionUUID);
        if (session == null)
            throw new ResponseException("Session Expired");

        AuthUserCredentialDao userCredentialDao = authUserCredentialRepository.findById(session.getAuthUserCredentialId());
        if (userCredentialDao == null)
            throw new ResponseException("Invalid Credential");

        AuthClientDao clientDao = authClientRepository.findById(userCredentialDao.getAuthClientId());
        if (clientDao == null)
            throw new ResponseException("Invalid Client");
        KeycloakUserDetailsDto userDetailsDto = keycloakService.getUserDetailsForToken(RequestContext.getAccessToken(), clientDao);
        if (userDetailsDto == null)
            throw new ResponseException("Invalid Access Token");
        AuthUserDao user = authUserRepository.findById(userCredentialDao.getAuthUserId());
        if (user == null)
            throw new ResponseException("User does not exist");

        OrganizationDao organizationDao = organizationRepository.findById(clientDao.getOrganizationId());

        return SessionDetailsDto.builder()
                .details(SessionDto.builder()
                        .firstName(userDetailsDto.getGivenName())
                        .lastName(userDetailsDto.getFamilyName())
                        .email(user.getEmail())
                        .uuid(session.getUuid())
                        .phone(user.getPhoneNumber())
                        .authUUID(user.getUuid())
                        .organizationUUID(organizationDao.getUuid())
                        .build())
                .uuid(session.getUuid()).build();
    }

    @Override
    public Boolean deleteSession(String sessionUUID) {
        AuthUserSessionDao session = authUserSessionRepository.findByUUID(sessionUUID);
        if (session == null)
            throw new ResponseException("Session Expired");

        AuthUserCredentialDao userCredentialDao = authUserCredentialRepository.findById(session.getAuthUserCredentialId());
        if (userCredentialDao == null)
            throw new ResponseException("Invalid Credential");

        AuthClientDao clientDao = authClientRepository.findById(userCredentialDao.getAuthClientId());
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        keycloakService.logout(session.getIdToken(), clientDao);
        return true;
    }

    @Override
    public AuthCredentialDto getSessionForSessionType(String phoneNumber, String credentialTypeUUID, Boolean resend, String resendType) {

        AuthClientDao clientDao = RequestContext.getClient();
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        AuthCredentialTypeDao credentialTypeDao = authCredentialTypeRepository.findByUUID(credentialTypeUUID);
        if (credentialTypeDao == null)
            throw new ResponseException("Invalid Credential Type");

        AuthClientCredentialTypeMappingDao mapping = authClientCredentialTypeMappingRepository.findByClientIdAndCredentialTypeId(clientDao.getId(), credentialTypeDao.getId());
        if (mapping == null)
            throw new ResponseException("Invalid Credential Type");

        AuthUserDao authUser = authUserRepository.findByPhoneNumber(phoneNumber);

        if(Boolean.TRUE.equals(resend)){
            if(!Objects.equals(ApplicationConstants.ADMIN_USER,phoneNumber))
                messageUtils.resendMessage(clientDao, credentialTypeDao,authUser, ResendType.valueOf(resendType));
            return AuthCredentialDto.builder()
                    .authKey(phoneNumber)
                    .authCredentialType(AuthCredentialTypeDto.builder()
                            .name(credentialTypeDao.getName())
                            .uuid(credentialTypeDao.getUuid())
                            .build())
                    .authUUID(authUser.getUuid())
                    .build();
        }

        String keycloakId = null;
        if (authUser == null) {
            if (!Boolean.TRUE.equals(clientDao.getIsUserRegistrationAllowed()))
                throw new ResponseException("User with phone Number :" + phoneNumber + " does not exist.");
            authUser = new AuthUserDao();
            authUser.setPhoneNumber(phoneNumber);
            authUser = authUserRepository.save(authUser);
        }


        AuthUserCredentialDao credentialDao = authUserCredentialRepository.findByAuthUserIdAndCredentialTypeId(authUser.getId(), credentialTypeDao.getId());
        if (credentialDao == null) {

            String uuid = UUID.randomUUID().toString();
            credentialDao = new AuthUserCredentialDao();

            credentialDao.setAuthClientId(clientDao.getId());
            credentialDao.setAuthCredentialTypeId(credentialTypeDao.getId());
            credentialDao.setAuthUserId(authUser.getId());
            credentialDao.setAuthKey(uuid);
            credentialDao.setUuid(uuid);
            credentialDao.setAuthCredentialTypeId(credentialTypeDao.getId());
            credentialDao = authUserCredentialRepository.save(credentialDao);
            keycloakId = keycloakService.addUser(credentialDao, clientDao,authUser.getEmail());

            credentialDao.setKeycloakUuid(keycloakId);
            credentialDao = authUserCredentialRepository.update(credentialDao);

        }


        String secret = SecretGenerationUtils.getSecret(6);


        credentialDao.setAuthSecret(Md5Utils.getMd5(secret));
        if(ApplicationConstants.ADMIN_USER!=null && ApplicationConstants.ADMIN_USER.equals(phoneNumber)){
            credentialDao.setAuthSecret(Md5Utils.getMd5(ApplicationConstants.FIXED_SECRET));
        }

        credentialDao = authUserCredentialRepository.update(credentialDao);
        keycloakService.updateUser(credentialDao, clientDao);

        if(!Objects.equals(ApplicationConstants.ADMIN_USER,phoneNumber))
            messageUtils.sendMessage(clientDao, credentialTypeDao,authUser, secret);

        AuthCredentialDto result = AuthCredentialDto.builder()
                .authKey(phoneNumber)
                .authCredentialType(AuthCredentialTypeDto.builder()
                        .name(credentialTypeDao.getName())
                        .uuid(credentialTypeDao.getUuid())
                        .build())
                .authUUID(authUser.getUuid())
                .build();


        if (Boolean.TRUE.equals(ApplicationConstants.SHOW_SECRETS))
            result.setAuthSecret(secret);
        return result;
    }


}