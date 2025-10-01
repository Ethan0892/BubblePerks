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
    // Simple hex pattern for legacy style (#RRGGBB inside <#xxxxxx>)
    private static final java.util.regex.Pattern MINI_HEX_PATTERN = java.util.regex.Pattern.compile("<#[0-9a-fA-F]{6}>");
    
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
        // Fast path: if it contains legacy codes but no MiniMessage angle brackets, treat as legacy only
        boolean looksLikeLegacy = text.indexOf('&') >= 0 && !text.contains("<");
        if (looksLikeLegacy) {
            return LEGACY_SERIALIZER.deserialize(ChatColor.translateAlternateColorCodes('&', text));
        }

    // Replace leftover <#RRGGBB> tags if we need to map them for legacy environments.
        // If we ultimately render to legacy later, convert them to nearest color (optional future enhancement)

        // Attempt MiniMessage parse first
        try {
            return MINI_MESSAGE.deserialize(text);
        } catch (Exception ignored) {
            // Fallback: strip mini tags we recognize and convert to legacy
            String legacy = fallbackMiniToLegacy(text);
            return LEGACY_SERIALIZER.deserialize(ChatColor.translateAlternateColorCodes('&', legacy));
        }
    }

    /**
     * Unified method returning a legacy formatted String for Bukkit chat APIs.
     */
    public static String colorizeUnified(String text) {
        if (text == null) return "";
        // Try component path then serialize to legacy
        return toLegacy(colorizeComponent(text));
    }

    /**
     * Convert a limited subset of MiniMessage style tags to legacy codes when MiniMessage fails.
     */
    private static String fallbackMiniToLegacy(String input) {
        String out = input;
        // Basic named colors to & codes
        out = out.replace("<red>", "&c")
                 .replace("<green>", "&a")
                 .replace("<yellow>", "&e")
                 .replace("<gold>", "&6")
                 .replace("<aqua>", "&b")
                 .replace("<white>", "&f")
                 .replace("<gray>", "&7")
                 .replace("<dark_gray>", "&8")
                 .replace("<light_purple>", "&d")
                 .replace("<blue>", "&9")
                 .replace("<bold>", "&l")
                 .replace("<b>", "&l")
                 .replace("</bold>", "")
                 .replace("</b>", "")
                 .replace("<italic>", "&o").replace("</italic>", "")
                 .replace("<underlined>", "&n").replace("</underlined>", "")
                 .replace("<obfuscated>", "&k").replace("</obfuscated>", "")
                 .replace("<strikethrough>", "&m").replace("</strikethrough>", "");

        // Remove simple gradient wrappers keeping inner text
        out = out.replaceAll("<gradient:[^>]+>", "").replaceAll("</gradient>", "");

        // Convert hex <#RRGGBB> tags to closest legacy (approx mapping by hue)
        java.util.regex.Matcher m = MINI_HEX_PATTERN.matcher(out);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String tag = m.group();
            String hex = tag.substring(2, 8); // inside #xxxxxx
            String replacement = approximateLegacyFromHex(hex);
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String approximateLegacyFromHex(String hex) {
        try {
            int r = Integer.parseInt(hex.substring(0,2),16);
            int g = Integer.parseInt(hex.substring(2,4),16);
            int b = Integer.parseInt(hex.substring(4,6),16);
            // Simple luminance & hue heuristic
            if (r > 200 && g > 200 && b > 200) return "&f"; // white
            if (r > 200 && g > 200 && b < 80) return "&e"; // yellow
            if (r > 200 && g < 100 && b < 100) return "&c"; // red
            if (r < 100 && g > 180 && b > 180) return "&b"; // aqua
            if (r < 120 && g > 180 && b < 120) return "&a"; // green
            if (r < 120 && g < 120 && b > 180) return "&9"; // blue
            if (r > 180 && g < 120 && b > 180) return "&d"; // light purple
            if (r > 150 && g > 100 && b < 60) return "&6"; // gold
            if (r < 120 && g < 120 && b < 120) return "&8"; // dark gray
            return "&7"; // default gray
        } catch (Exception e) {
            return "&7";
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
