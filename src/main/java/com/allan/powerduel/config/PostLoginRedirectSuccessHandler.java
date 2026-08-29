package com.allan.powerduel.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * Pairs with PostLoginRedirectEntryPoint: reads the path stashed in the
 * session (if the user was sent here from a specific protected page), and
 * redirects there using our own known-correct base URL - never Spring's
 * request-derived absolute URL, which has proven unreliable here.
 */
public class PostLoginRedirectSuccessHandler implements AuthenticationSuccessHandler {

    private final String baseUrl;
    private final String defaultRedirect;

    public PostLoginRedirectSuccessHandler(String baseUrl, String defaultRedirect) {
        this.baseUrl = baseUrl;
        this.defaultRedirect = defaultRedirect;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        Object savedPath = request.getSession().getAttribute(PostLoginRedirectEntryPoint.SESSION_ATTR);
        request.getSession().removeAttribute(PostLoginRedirectEntryPoint.SESSION_ATTR);

        String target = (savedPath != null) ? baseUrl + savedPath.toString() : defaultRedirect;
        response.sendRedirect(target);
    }
}
