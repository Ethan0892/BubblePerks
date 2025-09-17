package com.bubblecraft.perks.commands;

import com.bubblecraft.perks.BubblePerks;
import com.bubblecraft.perks.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Main command handler for /perks
 */
public class PerksCommand implements CommandExecutor, TabCompleter {
    
    private final BubblePerks plugin;
    
    public PerksCommand(BubblePerks plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                           @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.colorize("&cThis command can only be used by players!"));
            return true;
        }
        
        if (!player.hasPermission("bubbleperks.use")) {
            player.sendMessage(ColorUtils.colorize("&cYou don't have permission to use this command!"));
            return true;
        }
        
        // Handle subcommands
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            
            switch (subCommand) {
                case "reload":
                    return handleReload(player);
                case "help":
                    return handleHelp(player);
                default:
                    break;
            }
        }
        
        // Open GUI
        plugin.getPerksGUI().openGUI(player);
        return true;
    }
    
    private boolean handleReload(Player player) {
        if (!player.hasPermission("bubbleperks.admin")) {
            player.sendMessage(ColorUtils.colorize("&cYou don't have permission to reload the plugin!"));
            return true;
        }
        
        try {
            plugin.getConfigManager().reloadConfigs();
            plugin.getPerkManager().reload();
            player.sendMessage(ColorUtils.colorize("&aBubblePerks has been reloaded successfully!"));
        } catch (Exception e) {
            player.sendMessage(ColorUtils.colorize("&cFailed to reload BubblePerks: " + e.getMessage()));
            plugin.getLogger().severe("Failed to reload plugin: " + e.getMessage());
        }
        
        return true;
    }
    
    private boolean handleHelp(Player player) {
        player.sendMessage(ColorUtils.colorize("&6&l=== BubblePerks Help ==="));
        player.sendMessage(ColorUtils.colorize("&e/perks &7- Open the perks GUI"));
        
        if (player.hasPermission("bubbleperks.admin")) {
            player.sendMessage(ColorUtils.colorize("&e/perks reload &7- Reload the plugin"));
        }
        
        player.sendMessage(ColorUtils.colorize("&e/perks help &7- Show this help message"));
        
        return true;
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, 
                                               @NotNull String alias, @NotNull String[] args) {
        
        if (args.length == 1) {
            List<String> completions = Arrays.asList("help");
            
            if (sender.hasPermission("bubbleperks.admin")) {
                completions = Arrays.asList("help", "reload");
            }
            
            return completions.stream()
                    .filter(completion -> completion.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        
        return null;
    }
}
