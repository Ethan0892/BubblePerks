package com.bubblecraft.perks.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

/**
 * Utility class for handling color codes and text formatting
 */
public class ColorUtils {
    
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
    
    /**
     * Colorize text using legacy color codes (&)
     */
    public static String colorize(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    /**
     * Colorize text using MiniMessage format
     */
    public static Component colorizeComponent(String text) {
        if (text == null) return Component.empty();
        try {
            return MINI_MESSAGE.deserialize(text);
        } catch (Exception e) {
            // Fallback to legacy if MiniMessage fails
            return LEGACY_SERIALIZER.deserialize(text);
        }
    }
    
    /**
     * Strip all color codes from text
     */
    public static String stripColor(String text) {
        if (text == null) return "";
        return ChatColor.stripColor(text);
    }
    
    /**
     * Convert Component to legacy string
     */
    public static String toLegacy(Component component) {
        return LEGACY_SERIALIZER.serialize(component);
    }
    
    /**
     * Convert legacy string to Component
     */
    public static Component fromLegacy(String text) {
        return LEGACY_SERIALIZER.deserialize(text);
    }
}
