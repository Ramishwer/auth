package com.goev.auth.service.session;


import com.goev.auth.dto.client.AuthCredentialDto;
import com.goev.auth.dto.session.ExchangeTokenRequestDto;
import com.goev.auth.dto.session.SessionDetailsDto;
import com.goev.auth.dto.session.SessionDto;

public interface SessionService {
    SessionDto createSession(AuthCredentialDto credentials);

    SessionDto createSession(ExchangeTokenRequestDto token);

    SessionDto refreshSessionForSessionUUID(String sessionUUID);

    SessionDetailsDto getSessionDetails(String sessionUUID);

    Boolean deleteSession(String sessionUUID);

    AuthCredentialDto getSessionForSessionType(String phoneNumber, String credentialType);
}
