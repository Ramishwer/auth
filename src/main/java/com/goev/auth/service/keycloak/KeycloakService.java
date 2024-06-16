package com.goev.auth.service.keycloak;

import com.goev.auth.dao.client.AuthClientDao;
import com.goev.auth.dao.user.AuthUserCredentialDao;
import com.goev.auth.dto.client.AuthCredentialDto;
import com.goev.auth.dto.keycloak.KeycloakTokenDto;
import com.goev.auth.dto.keycloak.KeycloakUserDetailsDto;
import com.goev.auth.dto.session.ExchangeTokenRequestDto;

public interface KeycloakService {
    KeycloakTokenDto getTokenForRefreshToken(String refreshToken, AuthClientDao client);

    KeycloakTokenDto getExchangeTokenForToken(ExchangeTokenRequestDto tokenRequest, AuthClientDao client);

    KeycloakUserDetailsDto getUserDetailsForToken(String accessToken, AuthClientDao client);

    boolean updateUser(AuthUserCredentialDao authUserCredentialDao, AuthClientDao client);

    String addUser(AuthUserCredentialDao authUserCredentialDao, AuthClientDao client);

    String addUserForEmail(AuthUserCredentialDao authUserCredentialDao, AuthClientDao client, String email);

    KeycloakTokenDto login(AuthCredentialDto credentials, AuthClientDao client);

    boolean logout(String idToken, AuthClientDao client);
}
