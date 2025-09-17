package com.bubblecraft.perks.config;

import com.bubblecraft.perks.BubblePerks;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Manages plugin configuration files
 */
public class ConfigManager {
    
    private final BubblePerks plugin;
    private FileConfiguration config;
    private FileConfiguration perksConfig;
    private FileConfiguration messagesConfig;
    
    private File configFile;
    private File perksFile;
    private File messagesFile;
    
    public ConfigManager(BubblePerks plugin) {
        this.plugin = plugin;
        loadConfigs();
    }
    
    private void loadConfigs() {
        // Main config
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        config = plugin.getConfig();
        
        // Perks config
        perksFile = new File(plugin.getDataFolder(), "perks.yml");
        if (!perksFile.exists()) {
            plugin.saveResource("perks.yml", false);
        }
        perksConfig = YamlConfiguration.loadConfiguration(perksFile);
        
        // Messages config
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        
        // Load defaults
        loadDefaults();
    }
    
    private void loadDefaults() {
        // Load default configs from resources
        try {
            InputStream perksStream = plugin.getResource("perks.yml");
            if (perksStream != null) {
                YamlConfiguration defaultPerks = YamlConfiguration.loadConfiguration(new InputStreamReader(perksStream));
                perksConfig.setDefaults(defaultPerks);
            }
            
            InputStream messagesStream = plugin.getResource("messages.yml");
            if (messagesStream != null) {
                YamlConfiguration defaultMessages = YamlConfiguration.loadConfiguration(new InputStreamReader(messagesStream));
                messagesConfig.setDefaults(defaultMessages);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Could not load default configurations", e);
        }
    }
    
    public void reloadConfigs() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        perksConfig = YamlConfiguration.loadConfiguration(perksFile);
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        loadDefaults();
    }
    
    public void saveConfigs() {
        try {
            plugin.saveConfig();
            perksConfig.save(perksFile);
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save configurations", e);
        }
    }
    
    // Getters
    public FileConfiguration getConfig() {
        return config;
    }
    
    public FileConfiguration getPerksConfig() {
        return perksConfig;
    }
    
    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }
}
