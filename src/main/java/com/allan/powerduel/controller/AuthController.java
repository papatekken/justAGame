package com.allan.powerduel.controller;

import com.allan.powerduel.dto.PlayerResponse;
import com.allan.powerduel.model.Player;
import com.allan.powerduel.repository.PlayerRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final PlayerRepository playerRepository;

    public AuthController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * The game screen calls this on load. Signed-out visitors get a plain
     * "authenticated: false" (200, not 401) so the frontend can just branch
     * on the JSON instead of handling an error response for the normal case.
     */
    @GetMapping("/api/auth/me")
    public PlayerResponse me(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return PlayerResponse.signedOut();
        }

        String googleSub = principal.getAttribute("sub");
        return playerRepository.findByGoogleSub(googleSub)
                .map(p -> PlayerResponse.of(p.getDisplayName(), p.getPowerLevel(), p.getTrainingLevel()))
                .orElse(PlayerResponse.signedOut());
    }
}
