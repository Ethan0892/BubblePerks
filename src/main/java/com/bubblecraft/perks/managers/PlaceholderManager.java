package com.bubblecraft.perks.managers;

import com.bubblecraft.perks.BubblePerks;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI integration for BubblePerks
 */
public class PlaceholderManager extends PlaceholderExpansion {
    
    private final BubblePerks plugin;
    
    public PlaceholderManager(BubblePerks plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "bubbleperks";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "BubbleCraft";
    }
    
    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        
        // Only works for online players since we need to check permissions
        if (!(player instanceof Player onlinePlayer)) {
            return "offline";
        }
        
        // %bubbleperks_has_<perkid>%
        if (params.startsWith("has_")) {
            String perkId = params.substring(4);
            return String.valueOf(plugin.getPerkManager().hasPlayerPurchased(onlinePlayer, perkId));
        }
        
        // %bubbleperks_level_<perkid>%
        if (params.startsWith("level_")) {
            String perkId = params.substring(6);
            return String.valueOf(plugin.getPerkManager().getPlayerPerkLevel(onlinePlayer, perkId));
        }
        
        // %bubbleperks_total_perks%
        if (params.equals("total_perks")) {
            return String.valueOf(plugin.getPerkManager().getPlayerPerks(onlinePlayer).size());
        }
        
        return null;
    }
}
