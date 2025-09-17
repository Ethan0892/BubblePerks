package com.bubblecraft.perks.economy;

import com.bubblecraft.perks.BubblePerks;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Manages economy integration with Vault
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
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        
        economy = rsp.getProvider();
        return economy != null;
    }
    
    /**
     * Check if economy is available
     */
    public boolean isEconomyAvailable() {
        return economy != null;
    }
    
    /**
     * Get player's balance
     */
    public double getBalance(OfflinePlayer player) {
        if (!isEconomyAvailable()) return 0.0;
        return economy.getBalance(player);
    }
    
    /**
     * Check if player has enough money
     */
    public boolean hasEnough(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }
    
    /**
     * Withdraw money from player
     */
    public boolean withdraw(OfflinePlayer player, double amount) {
        if (!isEconomyAvailable()) return false;
        if (!hasEnough(player, amount)) return false;
        
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }
    
    /**
     * Deposit money to player
     */
    public boolean deposit(OfflinePlayer player, double amount) {
        if (!isEconomyAvailable()) return false;
        return economy.depositPlayer(player, amount).transactionSuccess();
    }
    
    /**
     * Format currency amount
     */
    public String format(double amount) {
        if (!isEconomyAvailable()) return String.valueOf(amount);
        return economy.format(amount);
    }
    
    /**
     * Get currency name
     */
    public String getCurrencyName() {
        if (!isEconomyAvailable()) return "coins";
        return economy.currencyNamePlural();
    }
}
