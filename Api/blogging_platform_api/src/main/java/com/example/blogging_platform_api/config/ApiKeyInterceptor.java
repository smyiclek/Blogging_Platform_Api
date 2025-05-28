package com.example.blogging_platform_api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${blog.api.key}")
    private String configuredApiKey;

    private static final String API_KEY_HEADER_NAME = "X-API-KEY";

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        String providedApiKey = request.getHeader(API_KEY_HEADER_NAME);

        if (providedApiKey == null || !providedApiKey.equals(configuredApiKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or Missing API Key");
            return false;
        }
        return true;
    }
}