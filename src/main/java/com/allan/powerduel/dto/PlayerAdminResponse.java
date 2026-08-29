package com.allan.powerduel.dto;

import com.allan.powerduel.model.Player;

import java.time.Instant;

public class PlayerAdminResponse {

    private final Long id;
    private final String displayName;
    private final String email;
    private final Integer powerLevel;
    private final Integer trainingLevel;
    private final Instant createdAt;
    private final Instant lastLoginAt;

    private PlayerAdminResponse(Player p) {
        this.id = p.getId();
        this.displayName = p.getDisplayName();
        this.email = p.getEmail();
        this.powerLevel = p.getPowerLevel();
        this.trainingLevel = p.getTrainingLevel();
        this.createdAt = p.getCreatedAt();
        this.lastLoginAt = p.getLastLoginAt();
    }

    public static PlayerAdminResponse of(Player p) {
        return new PlayerAdminResponse(p);
    }

    public Long getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public Integer getPowerLevel() { return powerLevel; }
    public Integer getTrainingLevel() { return trainingLevel; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastLoginAt() { return lastLoginAt; }
}
