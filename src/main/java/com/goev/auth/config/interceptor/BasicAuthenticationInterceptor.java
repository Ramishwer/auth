package com.goev.auth.config.interceptor;


import com.goev.auth.dao.auth.AuthClientDao;
import com.goev.auth.repository.auth.AuthClientRepository;
import com.goev.auth.utilities.RequestContext;
import com.goev.lib.exceptions.ResponseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;


@Component
@Slf4j
@AllArgsConstructor
public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    private final AuthClientRepository authClientRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new ResponseException("Invalid Access Token");
        }
        String tokenType = authorizationHeader.split(" ")[0];

        if (tokenType == null || !tokenType.toLowerCase().startsWith("basic"))
            throw new ResponseException("Invalid Access Token");
        String token = authorizationHeader.split(" ")[1];

        if (token == null)
            throw new ResponseException("Invalid Access Token");

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedString = new String(decodedBytes);
        String clientId = decodedString.split(":")[0];
        String clientSecret = decodedString.split(":")[1];

        AuthClientDao clientDao = authClientRepository.findByClientIdAndClientSecret(clientId, clientSecret);
        if (clientDao == null)
            throw new ResponseException("Invalid Client");
        request.setAttribute("client",clientDao);
        request.setAttribute("organizationId",clientDao.getOrganizationId());

        /** Code to Authenticate */


        return true;
    }

}
