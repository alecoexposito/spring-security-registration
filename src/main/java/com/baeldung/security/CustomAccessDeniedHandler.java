package com.baeldung.security;

import com.baeldung.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String username = getUsername((Authentication) request.getUserPrincipal());
        String url = request.getRequestURL().toString();
        LOGGER.info("User {} attempted to access unauthorized URL {}", username, url);
        response.sendRedirect("/accessDenied.html");
    }

    public String getUsername(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getEmail();
        }
        return authentication.getName();
    }
}
