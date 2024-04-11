package com.goev.auth.service.session;


import com.goev.auth.dto.auth.AuthCredentialDto;
import com.goev.auth.dto.session.SessionDetailsDto;
import com.goev.auth.dto.session.SessionDto;

public interface SessionService {
    SessionDto createSession(AuthCredentialDto credentials);
    SessionDto refreshSessionForSessionUUID(String sessionUUID);
    SessionDetailsDto getSessionDetails(String sessionUUID);
    Boolean deleteSession(String sessionUUID);
    AuthCredentialDto getSessionForSessionType(String phoneNumber, String credentialType);
}
