package com.allan.powerduel.config;

import com.allan.powerduel.service.PlayerOAuth2UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final PlayerOAuth2UserService playerOAuth2UserService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.frontend-success-redirect}")
    private String frontendSuccessRedirect;

    public SecurityConfig(PlayerOAuth2UserService playerOAuth2UserService) {
        this.playerOAuth2UserService = playerOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // The game page and its static assets, plus the "am I logged in" check,
                // are reachable without being authenticated - the page itself decides
                // what to show based on the response.
                .requestMatchers("/", "/index.html", "/static/**", "/*.css", "/*.js",
                                  "/api/auth/me", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            // Spring's default entry point and post-login redirect both reconstruct
            // absolute URLs from the request (scheme/host/port) - that reconstruction
            // has proven unreliable behind Nginx in this deployment (drops the :8880
            // port). These two custom classes remember/redirect using only the raw
            // request path plus our own known-correct app.base-url, sidestepping the
            // reconstruction entirely rather than depending on forwarded headers.
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new PostLoginRedirectEntryPoint(baseUrl + "/oauth2/authorization/google"))
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(playerOAuth2UserService))
                .successHandler(new PostLoginRedirectSuccessHandler(baseUrl, frontendSuccessRedirect))
            )
            .logout(logout -> logout
                .logoutSuccessUrl(frontendSuccessRedirect)
            )
            // H2 console renders in a frame; CSRF is left on for everything else.
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/logout", "/api/admin/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
