package com.bubblecraft.perks.managers;

import com.bubblecraft.perks.BubblePerks;
import com.bubblecraft.perks.models.Perk;
import com.bubblecraft.perks.models.PerkType;
import com.bubblecraft.perks.models.PlayerPerk;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Manages all perk-related operations
 */
public class PerkManager {
    
    private final BubblePerks plugin;
    private final Map<String, Perk> perks;
    
    public PerkManager(BubblePerks plugin) {
        this.plugin = plugin;
        this.perks = new HashMap<>();
        loadPerks();
    }
    
    /**
     * Load perks from configuration
     */
    private void loadPerks() {
        ConfigurationSection perksSection = plugin.getConfigManager().getPerksConfig().getConfigurationSection("perks");
        if (perksSection == null) {
            plugin.getLogger().warning("No perks found in configuration!");
            return;
        }
        
        for (String perkId : perksSection.getKeys(false)) {
            ConfigurationSection perkSection = perksSection.getConfigurationSection(perkId);
            if (perkSection == null) continue;
            
            try {
                Perk perk = createPerkFromConfig(perkId, perkSection);
                perks.put(perkId, perk);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load perk: " + perkId + " - " + e.getMessage());
            }
        }
        
        plugin.getLogger().info("Loaded " + perks.size() + " perks");
    }
    
    /**
     * Create a perk object from configuration section
     */
    private Perk createPerkFromConfig(String id, ConfigurationSection section) {
        String name = section.getString("name", id);
        String description = section.getString("description", "");
        PerkType type = PerkType.valueOf(section.getString("type", "UTILITY").toUpperCase());
        double cost = section.getDouble("cost", 0.0);
        String permission = section.getString("permission", "");
        String material = section.getString("material", "STONE");
        List<String> lore = section.getStringList("lore");
        List<String> commands = section.getStringList("commands");
        
        Map<String, Object> requirements = new HashMap<>();
        ConfigurationSection reqSection = section.getConfigurationSection("requirements");
        if (reqSection != null) {
            for (String key : reqSection.getKeys(false)) {
                requirements.put(key, reqSection.get(key));
            }
        }
        
        Map<String, Object> rewards = new HashMap<>();
        ConfigurationSection rewardSection = section.getConfigurationSection("rewards");
        if (rewardSection != null) {
            for (String key : rewardSection.getKeys(false)) {
                rewards.put(key, rewardSection.get(key));
            }
        }
        
        boolean repeatable = section.getBoolean("repeatable", false);
        int maxLevel = section.getInt("max-level", 1);
        
        return new Perk(id, name, description, type, cost, permission, material, 
                       lore, commands, requirements, rewards, repeatable, maxLevel);
    }
    
    /**
     * Get all available perks
     */
    public Collection<Perk> getAllPerks() {
        return perks.values();
    }
    
    /**
     * Get perk by ID
     */
    public Perk getPerk(String id) {
        return perks.get(id);
    }
    
    /**
     * Get perks by type
     */
    public List<Perk> getPerksByType(PerkType type) {
        return perks.values().stream()
                .filter(perk -> perk.getType() == type)
                .toList();
    }
    
    /**
     * Check if player has purchased a perk by checking their permissions
     */
    public boolean hasPlayerPurchased(OfflinePlayer player, String perkId) {
        Perk perk = getPerk(perkId);
        if (perk == null) return false;
        
        // Check if player has the permission associated with this perk
        if (player instanceof Player onlinePlayer) {
            return onlinePlayer.hasPermission(perk.getPermission());
        }
        
        // For offline players, we can't check permissions directly
        // This would require a permission plugin API that supports offline checks
        return false;
    }
    
    /**
     * Get player's perk level by checking permission tiers
     */
    public int getPlayerPerkLevel(OfflinePlayer player, String perkId) {
        if (!(player instanceof Player onlinePlayer)) return 0;
        
        // For tiered perks, check the highest tier they have
        if (perkId.startsWith("homes_")) {
            if (onlinePlayer.hasPermission("essentials.sethome.multiple.tier3")) return 3;
            if (onlinePlayer.hasPermission("essentials.sethome.multiple.tier2")) return 2;
            if (onlinePlayer.hasPermission("essentials.sethome.multiple.tier1")) return 1;
            return 0;
        }
        
        if (perkId.startsWith("vault_")) {
            if (onlinePlayer.hasPermission("axvaults.vault.5")) return 3;
            if (onlinePlayer.hasPermission("axvaults.vault.3")) return 2;
            if (onlinePlayer.hasPermission("axvaults.vault.2")) return 1;
            return 0;
        }
        
        if (perkId.startsWith("job_")) {
            if (onlinePlayer.hasPermission("jobs.max.6")) return 3;
            if (onlinePlayer.hasPermission("jobs.max.5")) return 2;
            if (onlinePlayer.hasPermission("jobs.max.4")) return 1;
            return 0;
        }
        
        // For non-tiered perks, return 1 if they have it, 0 if they don't
        Perk perk = getPerk(perkId);
        if (perk != null && onlinePlayer.hasPermission(perk.getPermission())) {
            return 1;
        }
        
        return 0;
    }
    
    /**
     * Purchase a perk for a player
     */
    public boolean purchasePerk(Player player, String perkId) {
        Perk perk = getPerk(perkId);
        if (perk == null) return false;
        
        // Check if already purchased (has permission)
        if (player.hasPermission(perk.getPermission())) {
            return false; // Already has this perk
        }
        
        // Check economy
        if (!plugin.getEconomyManager().hasEnough(player, perk.getCost())) {
            return false;
        }
        
        // Check requirements
        if (!checkRequirements(player, perk)) {
            return false;
        }
        
        // Withdraw money
        if (!plugin.getEconomyManager().withdraw(player, perk.getCost())) {
            return false;
        }
        
        // Execute commands to grant permission (usually via LuckPerms)
        executeCommands(player, perk);
        
        // Apply immediate permission for this session
        player.addAttachment(plugin, perk.getPermission(), true);
        
        return true;
    }
    
    /**
     * Check if player meets perk requirements
     */
    private boolean checkRequirements(Player player, Perk perk) {
        Map<String, Object> requirements = perk.getRequirements();
        if (requirements.isEmpty()) return true;
        
        // Check permission requirements
        if (requirements.containsKey("permission")) {
            String requiredPerm = (String) requirements.get("permission");
            if (!player.hasPermission(requiredPerm)) {
                return false;
            }
        }
        
        // Check level requirements
        if (requirements.containsKey("level")) {
            int requiredLevel = (Integer) requirements.get("level");
            if (player.getLevel() < requiredLevel) {
                return false;
            }
        }
        
        // Add more requirement checks as needed
        
        return true;
    }
    
    /**
     * Execute perk commands
     */
    private void executeCommands(Player player, Perk perk) {
        for (String command : perk.getCommands()) {
            String processedCommand = command.replace("{player}", player.getName());
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), processedCommand);
        }
    }
    
    /**
     * Get all player perks by checking their permissions
     */
    public List<PlayerPerk> getPlayerPerks(OfflinePlayer player) {
        List<PlayerPerk> perks = new ArrayList<>();
        
        if (!(player instanceof Player onlinePlayer)) {
            return perks; // Can't check permissions for offline players
        }
        
        // Check all available perks to see which ones the player has
        for (Perk perk : getAllPerks()) {
            if (onlinePlayer.hasPermission(perk.getPermission())) {
                // Create a PlayerPerk object representing their ownership
                PlayerPerk playerPerk = new PlayerPerk(
                    player.getUniqueId(), 
                    perk.getId(), 
                    LocalDateTime.now() // We don't know the actual purchase date
                );
                perks.add(playerPerk);
            }
        }
        
        return perks;
    }
    
    /**
     * Reload perks
     */
    public void reload() {
        perks.clear();
        loadPerks();
    }
    
    /**
     * Shutdown manager
     */
    public void shutdown() {
        // Clean up resources
        perks.clear();
    }
}
