package com.rep.book.bookrepboot.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private static final Map<String, String> ROLE_URL_MAP = new HashMap<>() {{
        put("ROLE_USER", "/user/home");
        put("ROLE_ADMIN", "/admin/index");
        put("ROLE_DRIVER", "/admin/index");
        put("ROLE_SUPER_ADMIN", "/admin/index");
    }};

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess()");
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(ROLE_URL_MAP::containsKey)
                .findFirst()
                .orElse("");

        if (!role.isEmpty()) {
            response.sendRedirect(ROLE_URL_MAP.get(role));
            log.warn("***{} LOGGED IN***", role);
        } else {
            response.sendRedirect("/");
        }
    }
}