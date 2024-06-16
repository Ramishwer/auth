package com.goev.auth.service.keycloak.impl;

import com.goev.auth.dao.client.AuthClientDao;
import com.goev.auth.dao.user.AuthUserCredentialDao;
import com.goev.auth.dto.client.AuthCredentialDto;
import com.goev.auth.dto.keycloak.KeycloakTokenDto;
import com.goev.auth.dto.keycloak.KeycloakUserDetailsDto;
import com.goev.auth.dto.session.ExchangeTokenRequestDto;
import com.goev.auth.service.keycloak.KeycloakService;
import com.goev.lib.services.RestClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;


@Slf4j
@Service
@AllArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private static final String CHARSET_TEXT = "charset";
    private static final String CHARSET_VALUE = "utf-8";
    private static final String CLIENT_ID_TEXT = "client_id";
    private static final String CLIENT_SECRET_TEXT = "client_secret";
    private static final String REFRESH_TOKEN_TEXT = "refresh_token";
    private static final String GRANT_TYPE_TEXT = "grant_type";
    private static final String AUTHORISATION_TEXT = "Authorization";
    private static final String SUB_PATH = "/realms/";
    private static final String PASSWORD_TEXT = "password";


    private final RestClient restClient;
    private final Gson gson = new Gson();

    @Override
    public KeycloakTokenDto getTokenForRefreshToken(String refreshToken, AuthClientDao client) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add(CHARSET_TEXT, CHARSET_VALUE);
            MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
            formVars.add(REFRESH_TOKEN_TEXT, refreshToken);
            formVars.add(GRANT_TYPE_TEXT, REFRESH_TOKEN_TEXT);
            formVars.add(CLIENT_ID_TEXT, client.getClientKey());
            formVars.add(CLIENT_SECRET_TEXT, client.getClientAuthSecret());
            String url = client.getKeycloakUrl() + SUB_PATH + client.getRealm() + "/protocol/openid-connect/token";
            String response = restClient.post(url, headers, formVars, String.class, true);
            return gson.fromJson(response, new TypeToken<KeycloakTokenDto>() {
            }.getType());
        } catch (Exception e) {
            log.info("Exception occurred", e);
            throw e;
        }
    }

    @Override
    public KeycloakTokenDto getExchangeTokenForToken(ExchangeTokenRequestDto tokenRequest, AuthClientDao client) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add(CHARSET_TEXT, CHARSET_VALUE);
            MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
            formVars.add("audience", client.getClientKey());
            formVars.add("subject_token", tokenRequest.getAccessToken());
            formVars.add(GRANT_TYPE_TEXT, "urn:ietf:params:oauth:grant-type:token-exchange");
            formVars.add("scope", "openid");
            formVars.add(CLIENT_ID_TEXT, tokenRequest.getClientId());
            formVars.add(CLIENT_SECRET_TEXT, tokenRequest.getClientSecret());
            String url = client.getKeycloakUrl() + SUB_PATH + client.getRealm() + "/protocol/openid-connect/token";
            String response = restClient.post(url, headers, formVars, String.class, true);
            return gson.fromJson(response, new TypeToken<KeycloakTokenDto>() {
            }.getType());
        } catch (Exception e) {
            log.info("Exception occurred", e);
            throw e;
        }
    }

    @Override
    public KeycloakUserDetailsDto getUserDetailsForToken(String accessToken, AuthClientDao client) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(AUTHORISATION_TEXT, accessToken);

        String url = client.getKeycloakUrl() + SUB_PATH + client.getRealm() + "/protocol/openid-connect/userinfo";
        String response = restClient.get(url, headers, String.class, true);
        log.info("User details from Keycloak : {}", response);
        return new Gson().fromJson(response, new TypeToken<KeycloakUserDetailsDto>() {
        }.getType());
    }

    @Override
    public boolean updateUser(AuthUserCredentialDao authUserCredentialDao, AuthClientDao client) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(authUserCredentialDao.getAuthSecret());
        UserRepresentation user = new UserRepresentation();
        user.setCredentials(Collections.singletonList(credential));
        UsersResource usersResource = getInstance(client).realm(client.getRealm()).users();
        user.setEnabled(true);
        usersResource.get(authUserCredentialDao.getKeycloakUuid()).update(user);
        return true;
    }

    @Override
    public String addUser(AuthUserCredentialDao authUserCredentialDao, AuthClientDao client,String email) {
        UserRepresentation user = new UserRepresentation();
        user.setFirstName("AuthUser-" + authUserCredentialDao.getAuthUserId());
        user.setLastName(authUserCredentialDao.getUuid());
        user.setUsername(authUserCredentialDao.getAuthKey());
        user.setEmail(email);
        user.setEmailVerified(true);
        user.setEnabled(true);
        UsersResource usersResource = getInstance(client).realm(client.getRealm()).users();
        return CreatedResponseUtil.getCreatedId(usersResource.create(user));

    }


    @Override
    public KeycloakTokenDto login(AuthCredentialDto credentials, AuthClientDao client) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add(CHARSET_TEXT, CHARSET_VALUE);
            MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
            formVars.add("username", credentials.getAuthKey());
            formVars.add(PASSWORD_TEXT, credentials.getAuthSecret());
            formVars.add(GRANT_TYPE_TEXT, PASSWORD_TEXT);
            formVars.add(CLIENT_ID_TEXT, client.getClientKey());
            formVars.add(CLIENT_SECRET_TEXT, client.getClientAuthSecret());
            formVars.add("scope", "openid");
            String url = client.getKeycloakUrl() + SUB_PATH + client.getRealm() + "/protocol/openid-connect/token";
            String response = restClient.post(url, headers, formVars, String.class, true);
            log.info("Response from keycloak:{}", response);

            KeycloakTokenDto authResponse = new Gson().fromJson(response, new TypeToken<KeycloakTokenDto>() {
            }.getType());
            if (authResponse == null || authResponse.getAccessToken() == null)
                return null;
            return authResponse;

        } catch (Exception e) {
            log.info("Error Occurred", e);
            return null;
        }
    }

    @Override
    public boolean logout(String idToken, AuthClientDao client) {
        try {
            String url = client.getKeycloakUrl() + SUB_PATH + client.getRealm() + "/protocol/openid-connect/logout?id_token_hint=" + idToken;
            String response = restClient.get(url, new HttpHeaders(), String.class, true);
            log.info("Response: {}", response);
            return true;
        } catch (Exception e) {
            log.info("Exception in logout ", e);
            return false;
        }
    }

    private Keycloak getInstance(AuthClientDao client) {
        return KeycloakBuilder.builder()
                .serverUrl(client.getKeycloakUrl())
                .realm(client.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(client.getClientKey())
                .clientSecret(client.getClientAuthSecret())
                .username(client.getKeycloakAdminKey())
                .password(client.getKeycloakAdminSecret())
                .build();
    }
}
