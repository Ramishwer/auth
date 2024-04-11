package com.goev.auth.utilities;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContext {
    private RequestContext(){}

    public static String getClientId() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        String clientId = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            if (request.getAttribute("clientId") != null)
                clientId = (String) request.getAttribute("clientId");

        }
        return clientId;
    }

    public static String getClientSecret() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        String clientSecret = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (request.getAttribute("clientSecret") != null)
                clientSecret = (String) request.getAttribute("clientSecret");

        }
        return clientSecret;
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
        String clientSecret = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (request.getHeader("Refresh-Token") != null)
                clientSecret = request.getHeader("Refresh-Token");
        }
        return clientSecret;
    }

}
