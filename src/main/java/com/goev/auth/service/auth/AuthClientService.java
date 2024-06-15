package com.goev.auth.service.auth;

import com.goev.auth.dto.auth.AuthClientDto;
import com.goev.auth.dto.session.SessionDetailsDto;

public interface AuthClientService {
    AuthClientDto getClientDetails();
}
