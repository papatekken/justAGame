package com.allan.powerduel.dto;

public class PlayerUpdateRequest {

    private String displayName;
    private Integer powerLevel;
    private Integer trainingLevel;

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public Integer getPowerLevel() { return powerLevel; }
    public void setPowerLevel(Integer powerLevel) { this.powerLevel = powerLevel; }

    public Integer getTrainingLevel() { return trainingLevel; }
    public void setTrainingLevel(Integer trainingLevel) { this.trainingLevel = trainingLevel; }
}
