package com.goev.auth.utilities;

import com.goev.auth.dao.auth.AuthClientDao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContext {
    private RequestContext(){}

    public static AuthClientDao getClient() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        AuthClientDao client = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            if (request.getAttribute("client") != null)
                client = (AuthClientDao) request.getAttribute("client");

        }
        return client;
    }


    public static String getAccessToken() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        String clientSecret = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (request.getHeader("Authorization") != null)
                clientSecret = request.getHeader("Authorization");
        }
        return clientSecret;
    }

    public static String getRefreshToken() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        String refreshToken = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (request.getHeader("Refresh-Token") != null)
                refreshToken = request.getHeader("Refresh-Token");
        }
        return refreshToken;
    }

    public static String getOrganizationId() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        String organizationId = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (request.getAttribute("organizationId") != null)
                organizationId = (String) request.getAttribute("organizationId");

        }
        return organizationId;
    }
}
