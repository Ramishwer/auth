package com.goev.auth.service.session.impl;


import com.goev.auth.dao.auth.AuthClientCredentialTypeMappingDao;
import com.goev.auth.dao.auth.AuthClientDao;
import com.goev.auth.dao.auth.AuthCredentialTypeDao;
import com.goev.auth.dao.user.AuthUserCredentialDao;
import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.dao.user.AuthUserSessionDao;
import com.goev.auth.dto.auth.AuthCredentialDto;
import com.goev.auth.dto.auth.AuthCredentialTypeDto;
import com.goev.auth.dto.keycloak.KeycloakTokenDto;
import com.goev.auth.dto.keycloak.KeycloakUserDetailsDto;
import com.goev.auth.dto.session.SessionDetailsDto;
import com.goev.auth.dto.session.SessionDto;
import com.goev.auth.repository.auth.AuthClientCredentialTypeMappingRepository;
import com.goev.auth.repository.auth.AuthClientRepository;
import com.goev.auth.repository.auth.AuthCredentialTypeRepository;
import com.goev.auth.repository.user.AuthUserCredentialRepository;
import com.goev.auth.repository.user.AuthUserRepository;
import com.goev.auth.repository.user.AuthUserSessionRepository;
import com.goev.auth.service.keycloak.KeycloakService;
import com.goev.auth.service.session.SessionService;
import com.goev.auth.utilities.RequestContext;
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

    @Override
    public SessionDto createSession(AuthCredentialDto credentials) {

        String clientId = RequestContext.getClientId();
        String clientSecret = RequestContext.getClientSecret();

        AuthClientDao clientDao = authClientRepository.findByClientIdAndClientSecret(clientId, clientSecret);
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

        credentials.setAuthKey(credentialDao.getUuid());
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
        session = authUserSessionRepository.save(session);
        return SessionDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .authUUID(user.getUuid())
                .expiresIn(session.getExpiresIn())
                .uuid(session.getUuid())
                .build();
    }

    @Override
    public SessionDto refreshSessionForSessionUUID(String sessionUUID) {

        String clientId = RequestContext.getClientId();
        String clientSecret = RequestContext.getClientSecret();
        AuthClientDao clientDao = authClientRepository.findByClientIdAndClientSecret(clientId, clientSecret);
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        KeycloakTokenDto token = keycloakService.getTokenForRefreshToken(RequestContext.getRefreshToken(), clientDao);
        if (token == null)
            throw new ResponseException("Invalid Access Token");

        AuthUserSessionDao session = authUserSessionRepository.findByUUID(sessionUUID);
        if(session == null)
            throw new ResponseException("Session Expired");
        AuthUserSessionDao newSession = new AuthUserSessionDao();
        newSession.setAuthUserCredentialId(session.getAuthUserCredentialId());
        newSession.setRefreshToken(token.getAccessToken());
        newSession.setAccessToken(token.getRefreshToken());
        newSession.setExpiresIn(token.getExpiresIn());
        newSession.setRefreshExpiresIn(token.getRefreshExpiresIn());
        newSession.setAuthUserId(session.getAuthUserCredentialId());
        newSession = authUserSessionRepository.save(newSession);
        AuthUserDao user = authUserRepository.findById(session.getAuthUserId());

        if (user == null)
            throw new ResponseException("Invalid Credentials");

        return SessionDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .uuid(newSession.getUuid())
                .authUUID(user.getUuid())
                .build();
    }

    @Override
    public SessionDetailsDto getSessionDetails(String sessionUUID) {
        AuthUserSessionDao session = authUserSessionRepository.findByUUID(sessionUUID);
        if(session == null)
            throw new ResponseException("Session Expired");

        AuthUserCredentialDao userCredentialDao = authUserCredentialRepository.findById(session.getAuthUserCredentialId());
        if(userCredentialDao == null)
            throw new ResponseException("Invalid Credential");

        AuthClientDao clientDao = authClientRepository.findById(userCredentialDao.getAuthClientId());
        if (clientDao == null)
            throw new ResponseException("Invalid Client");
        KeycloakUserDetailsDto userDetailsDto = keycloakService.getUserDetailsForToken(RequestContext.getAccessToken(), clientDao);
        if (userDetailsDto == null)
            throw new ResponseException("Invalid Access Token");
        AuthUserDao user = authUserRepository.findById(userCredentialDao.getAuthUserId());

        return SessionDetailsDto.builder()
                .details(SessionDto.builder()
                        .firstName(userDetailsDto.getGivenName())
                        .lastName(userDetailsDto.getFamilyName())
                        .email(user.getEmail())
                        .uuid(session.getUuid())
                        .phone(user.getPhoneNumber())
                        .authUUID(user.getUuid())
                        .build())
                .uuid(session.getUuid()).build();
    }

    @Override
    public Boolean deleteSession(String sessionUUID) {
        AuthUserSessionDao session = authUserSessionRepository.findByUUID(sessionUUID);
        if(session == null)
            throw new ResponseException("Session Expired");

        AuthUserCredentialDao userCredentialDao = authUserCredentialRepository.findById(session.getAuthUserCredentialId());
        if(userCredentialDao == null)
            throw new ResponseException("Invalid Credential");

        AuthClientDao clientDao = authClientRepository.findById(userCredentialDao.getAuthClientId());
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        keycloakService.logout(KeycloakTokenDto.builder()
                .accessToken(RequestContext.getAccessToken())
                .refreshToken(RequestContext.getRefreshToken()).build(), clientDao);
        return true;
    }

    @Override
    public AuthCredentialDto getSessionForSessionType(String phoneNumber, String credentialTypeUUID) {

        String clientId = RequestContext.getClientId();
        String clientSecret = RequestContext.getClientSecret();

        AuthClientDao clientDao = authClientRepository.findByClientIdAndClientSecret(clientId, clientSecret);
        if (clientDao == null)
            throw new ResponseException("Invalid Client");

        AuthCredentialTypeDao credentialTypeDao = authCredentialTypeRepository.findByUUID(credentialTypeUUID);
        if (credentialTypeDao == null)
            throw new ResponseException("Invalid Credential Type");

        AuthClientCredentialTypeMappingDao mapping = authClientCredentialTypeMappingRepository.findByClientIdAndCredentialTypeId(clientDao.getId(), credentialTypeDao.getId());
        if (mapping == null)
            throw new ResponseException("Invalid Credential Type");

        AuthUserDao authUser = authUserRepository.findByPhoneNumber(phoneNumber);

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

            credentialDao = new AuthUserCredentialDao();

            credentialDao.setAuthClientId(clientDao.getId());
            credentialDao.setAuthCredentialTypeId(credentialTypeDao.getId());
            credentialDao.setAuthUserId(authUser.getId());
            credentialDao.setAuthCredentialTypeId(credentialTypeDao.getId());
            credentialDao = authUserCredentialRepository.save(credentialDao);
            keycloakId = keycloakService.addUser(credentialDao, clientDao);

            credentialDao.setKeycloakUuid(keycloakId);
            credentialDao = authUserCredentialRepository.update(credentialDao);

        }

        credentialDao.setAuthKey(credentialDao.getUuid());
        String secret = "123456";
        credentialDao.setAuthSecret(Md5Utils.getMd5(secret));
        credentialDao = authUserCredentialRepository.update(credentialDao);
        keycloakService.updateUser(credentialDao, clientDao);
        return AuthCredentialDto.builder()
                .authSecret(secret)
                .authKey(phoneNumber)
                .authCredentialType(AuthCredentialTypeDto.builder()
                        .name(credentialTypeDao.getName())
                        .uuid(credentialTypeDao.getUuid())
                        .build())
                .authUUID(authUser.getUuid())
                .build();
    }
}
