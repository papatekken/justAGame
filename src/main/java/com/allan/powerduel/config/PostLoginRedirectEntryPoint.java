package com.allan.powerduel.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;

/**
 * Remembers which page the user was trying to reach - by raw request PATH
 * only, e.g. "/admin.html", never a full reconstructed URL. Spring's default
 * SavedRequest mechanism reconstructs a full URL (scheme+host+port) from the
 * request, and that reconstruction has proven unreliable behind this
 * deployment's reverse proxy (drops the port). request.getRequestURI() comes
 * straight off the HTTP request line and is never affected by that.
 */
public class PostLoginRedirectEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public static final String SESSION_ATTR = "POST_LOGIN_REDIRECT_PATH";

    public PostLoginRedirectEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException, ServletException {
        String path = request.getRequestURI();
        if (request.getQueryString() != null) {
            path += "?" + request.getQueryString();
        }
        request.getSession().setAttribute(SESSION_ATTR, path);
        super.commence(request, response, authException);
    }
}
