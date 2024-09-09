package com.goev.auth.service.auth;

import com.goev.auth.dto.user.AuthUserDto;

public interface AuthUserService {
    AuthUserDto saveUser(AuthUserDto user);
    AuthUserDto updateUser(String authUUID,AuthUserDto user);
}
