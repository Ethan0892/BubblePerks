package com.bubblecraft.perks.economy;

import com.bubblecraft.perks.BubblePerks;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Economy manager using CoinsEngine API via reflection
 * This allows compilation without CoinsEngine dependency
 */
public class CoinsEngineManager {
    
    private final BubblePerks plugin;
    private Object coinsAPI;
    private Object defaultCurrency;
    private Method getBalanceMethod;
    private Method removeBalanceMethod;
    private Method addBalanceMethod;
    private Method formatMethod;
    private Method getCurrencyNameMethod;
    
    public CoinsEngineManager(BubblePerks plugin) {
        this.plugin = plugin;
        
        try {
            // Get CoinsEngineAPI class - v2.5.0 uses different package
            Class<?> coinsEngineAPIClass = Class.forName("su.nightexpress.coinsengine.api.CoinsEngineAPI");
            
            // Get Currency class
            Class<?> currencyClass = Class.forName("su.nightexpress.coinsengine.api.currency.Currency");
            
            // Get currency by ID from static method
            String currencyId = plugin.getConfig().getString("economy.coinsengine-currency", "coins");
            Method getCurrencyMethod = coinsEngineAPIClass.getMethod("getCurrency", String.class);
            this.defaultCurrency = getCurrencyMethod.invoke(null, currencyId);
            
            // If null, try to get first available currency
            if (this.defaultCurrency == null) {
                Method getCurrenciesMethod = coinsEngineAPIClass.getMethod("getCurrencies");
                Object currencies = getCurrenciesMethod.invoke(null);
                if (currencies instanceof java.util.Collection) {
                    this.defaultCurrency = ((java.util.Collection<?>) currencies).stream().findFirst().orElse(null);
                }
            }
            
            if (this.defaultCurrency != null) {
                // Cache methods for better performance - all methods are static in v2.5.0
                getBalanceMethod = coinsEngineAPIClass.getMethod("getBalance", UUID.class, currencyClass);
                removeBalanceMethod = coinsEngineAPIClass.getMethod("removeBalance", UUID.class, currencyClass, double.class);
                addBalanceMethod = coinsEngineAPIClass.getMethod("addBalance", UUID.class, currencyClass, double.class);
                formatMethod = currencyClass.getMethod("format", double.class);
                getCurrencyNameMethod = currencyClass.getMethod("getName");
                
                String currencyName = (String) getCurrencyNameMethod.invoke(defaultCurrency);
                plugin.getLogger().info("Hooked into CoinsEngine with currency: " + currencyName);
                
                // Set API to a marker (we use static methods, so we just need a non-null marker)
                this.coinsAPI = coinsEngineAPIClass;
            } else {
                plugin.getLogger().warning("CoinsEngine found but no currency available, falling back to Vault");
            }
            
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to hook into CoinsEngine: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            this.coinsAPI = null;
            this.defaultCurrency = null;
        }
    }
    
    /**
     * Check if CoinsEngine is available
     */
    public boolean isAvailable() {
        return coinsAPI != null && defaultCurrency != null;
    }
    
    /**
     * Get player's balance
     */
    public double getBalance(OfflinePlayer player) {
        if (!isAvailable()) {
            plugin.getLogger().warning("CoinsEngine not available when checking balance for " + player.getName());
            return 0.0;
        }
        
        try {
            // Call static method
            Object balance = getBalanceMethod.invoke(null, player.getUniqueId(), defaultCurrency);
            return balance instanceof Number ? ((Number) balance).doubleValue() : 0.0;
        } catch (Exception e) {
            plugin.getLogger().severe("Error getting CoinsEngine balance for " + player.getName() + ": " + e.getMessage());
            return 0.0;
        }
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
        if (!isAvailable()) {
            plugin.getLogger().warning("CoinsEngine not available when withdrawing from " + player.getName());
            return false;
        }
        
        try {
            UUID uuid = player.getUniqueId();
            double balanceBefore = getBalance(player);
            
            // Withdraw using CoinsEngine API - call static method
            removeBalanceMethod.invoke(null, uuid, defaultCurrency, amount);
            
            double balanceAfter = getBalance(player);
            
            return balanceAfter < balanceBefore;
        } catch (Exception e) {
            plugin.getLogger().severe("Error withdrawing from CoinsEngine for " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deposit money to player
     */
    public boolean deposit(OfflinePlayer player, double amount) {
        if (!isAvailable()) {
            plugin.getLogger().warning("CoinsEngine not available when depositing to " + player.getName());
            return false;
        }
        
        try {
            // Call static method
            addBalanceMethod.invoke(null, player.getUniqueId(), defaultCurrency, amount);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Error depositing to CoinsEngine for " + player.getName() + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Format currency amount
     */
    public String format(double amount) {
        if (!isAvailable()) {
            return String.format("%.2f", amount);
        }
        
        try {
            return (String) formatMethod.invoke(defaultCurrency, amount);
        } catch (Exception e) {
            return String.format("%.2f", amount);
        }
    }
    
    /**
     * Get currency name
     */
    public String getCurrencyName() {
        if (!isAvailable()) {
            return "Coins";
        }
        
        try {
            return (String) getCurrencyNameMethod.invoke(defaultCurrency);
        } catch (Exception e) {
            return "Coins";
        }
    }
}
