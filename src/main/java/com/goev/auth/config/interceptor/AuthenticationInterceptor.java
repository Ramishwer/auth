package com.goev.auth.config.interceptor;



import com.goev.lib.exceptions.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");

        if (token == null) {
            throw new ResponseException("Invalid Access Token");
        }
        /** Code to Authenticate */
        return true;
    }

}
