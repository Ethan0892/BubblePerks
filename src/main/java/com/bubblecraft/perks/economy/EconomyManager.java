package com.bubblecraft.perks.economy;

import com.bubblecraft.perks.BubblePerks;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Manages economy integration with Vault
 * Enhanced with better error handling and logging
 */
public class EconomyManager {
    
    private final BubblePerks plugin;
    private Economy economy;
    
    public EconomyManager(BubblePerks plugin) {
        this.plugin = plugin;
        setupEconomy();
    }
    
    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault not found! Economy features will not work.");
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("No economy provider found! Make sure you have an economy plugin installed.");
            return false;
        }
        
        economy = rsp.getProvider();
        if (economy != null) {
            plugin.getLogger().info("Hooked into economy: " + economy.getName());
        }
        return economy != null;
    }
    
    /**
     * Check if economy is available
     */
    public boolean isEconomyAvailable() {
        return economy != null;
    }
    
    /**
     * Get player's balance with error handling
     */
    public double getBalance(OfflinePlayer player) {
        // Use CoinsEngine if available
        if (plugin.isUsingCoinsEngine()) {
            return plugin.getCoinsEngineManager().getBalance(player);
        }
        
        if (!isEconomyAvailable()) {
            plugin.getLogger().warning("Economy not available when checking balance for " + player.getName());
            return 0.0;
        }
        
        try {
            double balance = economy.getBalance(player);
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Balance for " + player.getName() + ": " + balance);
            }
            
            return balance;
        } catch (Exception e) {
            plugin.getLogger().severe("Error getting balance for " + player.getName() + ": " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Check if player has enough money
     */
    public boolean hasEnough(OfflinePlayer player, double amount) {
        double balance = getBalance(player);
        return balance >= amount;
    }
    
    /**
     * Withdraw money from player with enhanced error handling
     */
    public boolean withdraw(OfflinePlayer player, double amount) {
        // Use CoinsEngine if available
        if (plugin.isUsingCoinsEngine()) {
            return plugin.getCoinsEngineManager().withdraw(player, amount);
        }
        
        if (!isEconomyAvailable()) {
            plugin.getLogger().warning("Economy not available when withdrawing from " + player.getName());
            return false;
        }
        
        if (!hasEnough(player, amount)) {
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Player " + player.getName() + " doesn't have enough: " + 
                    getBalance(player) + " < " + amount);
            }
            return false;
        }
        
        try {
            boolean success = economy.withdrawPlayer(player, amount).transactionSuccess();
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Withdraw " + amount + " from " + player.getName() + ": " + 
                    (success ? "SUCCESS" : "FAILED"));
                if (success) {
                    plugin.getLogger().info("New balance: " + getBalance(player));
                }
            }
            
            return success;
        } catch (Exception e) {
            plugin.getLogger().severe("Error withdrawing from " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deposit money to player
     */
    public boolean deposit(OfflinePlayer player, double amount) {
        if (!isEconomyAvailable()) return false;
        
        try {
            return economy.depositPlayer(player, amount).transactionSuccess();
        } catch (Exception e) {
            plugin.getLogger().severe("Error depositing to " + player.getName() + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Format currency amount
     */
    public String format(double amount) {
        // Use CoinsEngine if available
        if (plugin.isUsingCoinsEngine()) {
            return plugin.getCoinsEngineManager().format(amount);
        }
        
        if (!isEconomyAvailable()) return String.valueOf((int)amount);
        
        try {
            return economy.format(amount);
        } catch (Exception e) {
            return String.valueOf((int)amount);
        }
    }
    
    /**
     * Get currency name
     */
    public String getCurrencyName() {
        if (!isEconomyAvailable()) return "coins";
        
        try {
            return economy.currencyNamePlural();
        } catch (Exception e) {
            return "coins";
        }
    }
}
