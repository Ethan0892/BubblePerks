package com.bubblecraft.perks;

import com.bubblecraft.perks.commands.PerksCommand;
import com.bubblecraft.perks.config.ConfigManager;
import com.bubblecraft.perks.economy.CoinsEngineManager;
import com.bubblecraft.perks.economy.EconomyManager;
import com.bubblecraft.perks.gui.PerksGUI;
import com.bubblecraft.perks.listeners.InventoryListener;
import com.bubblecraft.perks.managers.PerkManager;
import com.bubblecraft.perks.managers.PlaceholderManager;
import com.bubblecraft.perks.utils.ColorUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class BubblePerks extends JavaPlugin {
    
    private static BubblePerks instance;
    private Logger logger;
    
    // Managers
    private ConfigManager configManager;
    private EconomyManager economyManager;
    private CoinsEngineManager coinsEngineManager;
    private PerkManager perkManager;
    private PlaceholderManager placeholderManager;
    private PerksGUI perksGUI;
    
    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        
        // Initialize plugin
        if (!initializePlugin()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        logger.info(ColorUtils.colorize("&a✓ BubblePerks has been enabled successfully!"));
    }
    
    @Override
    public void onDisable() {
        if (perkManager != null) {
            perkManager.shutdown();
        }
        
        logger.info(ColorUtils.colorize("&c✗ BubblePerks has been disabled!"));
    }
    
    private boolean initializePlugin() {
        try {
            // Initialize config first
            configManager = new ConfigManager(this);
            
            // Check for economy plugins
            boolean useCoinsEngine = getConfig().getBoolean("economy.use-coinsengine", true);
            boolean hasCoinsEngine = getServer().getPluginManager().getPlugin("CoinsEngine") != null;
            boolean hasVault = setupEconomy();
            
            // Try to use CoinsEngine if enabled and available
            if (useCoinsEngine && hasCoinsEngine) {
                try {
                    coinsEngineManager = new CoinsEngineManager(this);
                    if (coinsEngineManager.isAvailable()) {
                        logger.info("Using CoinsEngine for economy!");
                    } else {
                        logger.warning("CoinsEngine found but no currency available, falling back to Vault");
                        coinsEngineManager = null;
                    }
                } catch (Exception e) {
                    logger.warning("Failed to hook into CoinsEngine: " + e.getMessage());
                    logger.warning("Falling back to Vault economy");
                    coinsEngineManager = null;
                }
            }
            
            // Always initialize EconomyManager - it will delegate to CoinsEngine if available
            if (coinsEngineManager == null && !hasVault) {
                logger.severe("No economy plugin found! Please install Vault + an economy plugin, or CoinsEngine.");
                return false;
            }
            
            // EconomyManager handles delegation to CoinsEngine or Vault
            economyManager = new EconomyManager(this);
            if (coinsEngineManager == null) {
                logger.info("Using Vault for economy");
            }
            
            // Initialize other managers
            perkManager = new PerkManager(this);
            placeholderManager = new PlaceholderManager(this);
            perksGUI = new PerksGUI(this);
            
            // Register commands
            registerCommands();
            
            // Register listeners
            registerListeners();
            
            // Register PlaceholderAPI expansion if available
            if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                placeholderManager.register();
                logger.info("PlaceholderAPI integration enabled!");
            }
            
            return true;
            
        } catch (Exception e) {
            logger.severe("Failed to initialize plugin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        
        return true;
    }
    
    private void registerCommands() {
        getCommand("perks").setExecutor(new PerksCommand(this));
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
    }
    
    // Getters
    public static BubblePerks getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public EconomyManager getEconomyManager() {
        return economyManager;
    }
    
    public CoinsEngineManager getCoinsEngineManager() {
        return coinsEngineManager;
    }
    
    /**
     * Check if using CoinsEngine
     */
    public boolean isUsingCoinsEngine() {
        return coinsEngineManager != null && coinsEngineManager.isAvailable();
    }
    
    public PerkManager getPerkManager() {
        return perkManager;
    }
    
    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }
    
    public PerksGUI getPerksGUI() {
        return perksGUI;
    }
}
