package com.allan.powerduel.controller;

import com.allan.powerduel.dto.PlayerAdminResponse;
import com.allan.powerduel.dto.PlayerUpdateRequest;
import com.allan.powerduel.model.Player;
import com.allan.powerduel.repository.PlayerRepository;
import com.allan.powerduel.service.AdminAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/players")
public class AdminPlayerController {

    private final PlayerRepository playerRepository;
    private final AdminAccessService adminAccessService;

    public AdminPlayerController(PlayerRepository playerRepository, AdminAccessService adminAccessService) {
        this.playerRepository = playerRepository;
        this.adminAccessService = adminAccessService;
    }

    @GetMapping
    public ResponseEntity<?> list(@AuthenticationPrincipal OAuth2User principal) {
        if (!isAdmin(principal)) {
            return forbidden();
        }
        List<PlayerAdminResponse> players = playerRepository.findAll().stream()
                .map(PlayerAdminResponse::of)
                .toList();
        return ResponseEntity.ok(players);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                     @RequestBody PlayerUpdateRequest req,
                                     @AuthenticationPrincipal OAuth2User principal) {
        if (!isAdmin(principal)) {
            return forbidden();
        }
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        if (req.getDisplayName() != null && !req.getDisplayName().isBlank()) {
            player.setDisplayName(req.getDisplayName());
        }
        if (req.getPowerLevel() != null) {
            player.setPowerLevel(req.getPowerLevel());
        }
        if (req.getTrainingLevel() != null) {
            player.setTrainingLevel(req.getTrainingLevel());
        }
        playerRepository.save(player);
        return ResponseEntity.ok(PlayerAdminResponse.of(player));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        if (!isAdmin(principal)) {
            return forbidden();
        }
        if (!playerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(OAuth2User principal) {
        if (principal == null) {
            return false;
        }
        String email = principal.getAttribute("email");
        return adminAccessService.isAdmin(email);
    }

    private ResponseEntity<?> forbidden() {
        return ResponseEntity.status(403).body(Map.of("error", "forbidden"));
    }
}
