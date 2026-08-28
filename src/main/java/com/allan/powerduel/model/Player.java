package com.allan.powerduel.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Google's stable per-account identifier ("sub" claim) - never changes even if email/name do.
    @Column(nullable = false, unique = true)
    private String googleSub;

    @Column(nullable = false)
    private String displayName;

    private String email;

    @Column(nullable = false)
    private Integer powerLevel = 100;

    @Column(nullable = false)
    private Integer trainingLevel = 0;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant lastLoginAt = Instant.now();

    protected Player() {
        // JPA
    }

    public Player(String googleSub, String displayName, String email) {
        this.googleSub = googleSub;
        this.displayName = displayName;
        this.email = email;
    }

    public Long getId() { return id; }

    public String getGoogleSub() { return googleSub; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getPowerLevel() { return powerLevel; }
    public void setPowerLevel(Integer powerLevel) { this.powerLevel = powerLevel; }

    public Integer getTrainingLevel() { return trainingLevel; }
    public void setTrainingLevel(Integer trainingLevel) { this.trainingLevel = trainingLevel; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Instant lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
