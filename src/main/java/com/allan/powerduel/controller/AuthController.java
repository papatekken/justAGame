package com.allan.powerduel.controller;

import com.allan.powerduel.dto.PlayerResponse;
import com.allan.powerduel.repository.PlayerRepository;
import com.allan.powerduel.service.AdminAccessService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final PlayerRepository playerRepository;
    private final AdminAccessService adminAccessService;

    public AuthController(PlayerRepository playerRepository, AdminAccessService adminAccessService) {
        this.playerRepository = playerRepository;
        this.adminAccessService = adminAccessService;
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
        String email = principal.getAttribute("email");
        boolean admin = adminAccessService.isAdmin(email);

        return playerRepository.findByGoogleSub(googleSub)
                .map(p -> PlayerResponse.of(p.getDisplayName(), p.getPowerLevel(), p.getTrainingLevel(), admin))
                .orElse(PlayerResponse.signedOut());
    }
}
