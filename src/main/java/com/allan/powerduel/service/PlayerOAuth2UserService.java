package com.allan.powerduel.service;

import com.allan.powerduel.model.Player;
import com.allan.powerduel.repository.PlayerRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Google's login includes the "openid" scope, which makes this an OIDC login,
 * not a plain OAuth2 login. Spring Security routes OIDC logins through
 * OidcUserService specifically - a plain OAuth2UserService override (the
 * previous version of this class) is silently skipped for OIDC providers,
 * which is why no Player row was ever being created despite login working.
 */
@Service
public class PlayerOAuth2UserService extends OidcUserService {

    private final PlayerRepository playerRepository;

    public PlayerOAuth2UserService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String googleSub = oidcUser.getAttribute("sub");
        String name = oidcUser.getAttribute("name");
        String email = oidcUser.getAttribute("email");

        if (googleSub == null) {
            throw new OAuth2AuthenticationException("Google did not return a subject id");
        }

        Player player = playerRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> new Player(googleSub, name != null ? name : "Player", email));

        player.setDisplayName(name != null ? name : player.getDisplayName());
        player.setEmail(email);
        player.setLastLoginAt(Instant.now());
        playerRepository.save(player);

        return oidcUser;
    }
}
