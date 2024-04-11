package com.goev.auth.service.keycloak;

import com.goev.auth.dao.auth.AuthClientDao;
import com.goev.auth.dao.user.AuthUserCredentialDao;
import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.dto.auth.AuthCredentialDto;
import com.goev.auth.dto.keycloak.KeycloakTokenDto;
import com.goev.auth.dto.keycloak.KeycloakUserDetailsDto;

public interface KeycloakService {
    KeycloakTokenDto getTokenForRefreshToken(String refreshToken, AuthClientDao client);

    KeycloakUserDetailsDto getUserDetailsForToken(String accessToken, AuthClientDao client);

    boolean updateUser(AuthUserCredentialDao authUserCredentialDao, AuthClientDao client);

    String addUser(AuthUserCredentialDao authUserCredentialDao, AuthClientDao client);

    KeycloakTokenDto login(AuthCredentialDto credentials, AuthClientDao client);

    boolean logout(KeycloakTokenDto token, AuthClientDao client);
}
