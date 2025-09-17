package com.bubblecraft.perks.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a player's perk purchase
 */
public class PlayerPerk {
    
    private final UUID playerId;
    private final String perkId;
    private final LocalDateTime purchaseDate;
    private int level;
    private boolean active;
    
    public PlayerPerk(UUID playerId, String perkId, LocalDateTime purchaseDate) {
        this.playerId = playerId;
        this.perkId = perkId;
        this.purchaseDate = purchaseDate;
        this.level = 1;
        this.active = true;
    }
    
    public PlayerPerk(UUID playerId, String perkId, LocalDateTime purchaseDate, int level, boolean active) {
        this.playerId = playerId;
        this.perkId = perkId;
        this.purchaseDate = purchaseDate;
        this.level = level;
        this.active = active;
    }
    
    // Getters and Setters
    public UUID getPlayerId() { return playerId; }
    public String getPerkId() { return perkId; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public void incrementLevel() {
        this.level++;
    }
}
