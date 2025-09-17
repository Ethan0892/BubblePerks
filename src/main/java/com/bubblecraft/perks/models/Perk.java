package com.bubblecraft.perks.models;

import java.util.List;
import java.util.Map;

/**
 * Represents a purchasable perk
 */
public class Perk {
    
    private final String id;
    private final String name;
    private final String description;
    private final PerkType type;
    private final double cost;
    private final String permission;
    private final String material;
    private final List<String> lore;
    private final List<String> commands;
    private final Map<String, Object> requirements;
    private final Map<String, Object> rewards;
    private final boolean repeatable;
    private final int maxLevel;
    
    public Perk(String id, String name, String description, PerkType type, double cost, 
                String permission, String material, List<String> lore, List<String> commands,
                Map<String, Object> requirements, Map<String, Object> rewards, 
                boolean repeatable, int maxLevel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.cost = cost;
        this.permission = permission;
        this.material = material;
        this.lore = lore;
        this.commands = commands;
        this.requirements = requirements;
        this.rewards = rewards;
        this.repeatable = repeatable;
        this.maxLevel = maxLevel;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public PerkType getType() { return type; }
    public double getCost() { return cost; }
    public String getPermission() { return permission; }
    public String getMaterial() { return material; }
    public List<String> getLore() { return lore; }
    public List<String> getCommands() { return commands; }
    public Map<String, Object> getRequirements() { return requirements; }
    public Map<String, Object> getRewards() { return rewards; }
    public boolean isRepeatable() { return repeatable; }
    public int getMaxLevel() { return maxLevel; }
}
