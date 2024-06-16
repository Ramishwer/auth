package com.goev.auth.config.interceptor;


import com.goev.lib.exceptions.ResponseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
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

        if (tokenType == null || !tokenType.toLowerCase().startsWith("bearer"))
            throw new ResponseException("Invalid Access Token");
        String token = authorizationHeader.split(" ")[1];

        if (token == null)
            throw new ResponseException("Invalid Access Token");
        return true;
    }

}
