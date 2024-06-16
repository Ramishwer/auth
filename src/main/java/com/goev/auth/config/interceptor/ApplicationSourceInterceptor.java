package com.goev.auth.config.interceptor;


import com.goev.auth.config.SpringContext;
import com.goev.auth.constant.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class ApplicationSourceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ApplicationConstants applicationConstants = SpringContext.getBean(ApplicationConstants.class);
        request.setAttribute("applicationSource", applicationConstants.APPLICATION_ID);
        request.setAttribute("requestUUID", UUID.randomUUID().toString());
        return true;
    }
}