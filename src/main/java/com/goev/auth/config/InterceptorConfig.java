package com.goev.auth.config;


import com.goev.auth.config.interceptor.ApplicationSourceInterceptor;
import com.goev.auth.config.interceptor.AuthenticationInterceptor;
import com.goev.auth.config.interceptor.BasicAuthenticationInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@AllArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final BasicAuthenticationInterceptor basicAuthenticationInterceptor;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final ApplicationSourceInterceptor applicationSourceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(applicationSourceInterceptor);
        registry.addInterceptor(basicAuthenticationInterceptor).addPathPatterns("/**")
                .excludePathPatterns(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/api/v1/session-management/sessions/**",
                        "/api/v1/internal/events"
                );
        registry.addInterceptor(basicAuthenticationInterceptor).addPathPatterns(
                        "/api/v1/session-management/sessions",
                        "/api/v1/session-management/sessions/credential-types/**",
                        "/api/v1/session-management/sessions/**/token",
                        "/api/v1/session-management/sessions/tokens"
                );
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**")
                .excludePathPatterns(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/api/v1/session-management/sessions",
                        "/api/v1/session-management/sessions/credential-types/**",
                        "/api/v1/session-management/sessions/**/token",
                        "/api/v1/session-management/sessions/tokens",
                        "/api/v1/internal/events"
                );
    }
}