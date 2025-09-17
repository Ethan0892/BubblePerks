package com.bubblecraft.perks.models;

/**
 * Enum representing different types of perks
 */
public enum PerkType {
    COMMAND("Command", "Access to special commands"),
    UPGRADE("Upgrade", "Increase limits and capabilities"),
    COSMETIC("Cosmetic", "Visual enhancements"),
    UTILITY("Utility", "Quality of life improvements");
    
    private final String displayName;
    private final String description;
    
    PerkType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
