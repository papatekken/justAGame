package com.allan.powerduel.dto;

public class PlayerResponse {

    private final boolean authenticated;
    private final String name;
    private final Integer powerLevel;
    private final Integer trainingLevel;
    private final boolean admin;

    private PlayerResponse(boolean authenticated, String name, Integer powerLevel, Integer trainingLevel, boolean admin) {
        this.authenticated = authenticated;
        this.name = name;
        this.powerLevel = powerLevel;
        this.trainingLevel = trainingLevel;
        this.admin = admin;
    }

    public static PlayerResponse signedOut() {
        return new PlayerResponse(false, null, null, null, false);
    }

    public static PlayerResponse of(String name, Integer powerLevel, Integer trainingLevel, boolean admin) {
        return new PlayerResponse(true, name, powerLevel, trainingLevel, admin);
    }

    public boolean isAuthenticated() { return authenticated; }
    public String getName() { return name; }
    public Integer getPowerLevel() { return powerLevel; }
    public Integer getTrainingLevel() { return trainingLevel; }
    public boolean isAdmin() { return admin; }
}
