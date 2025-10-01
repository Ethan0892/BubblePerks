package com.bubblecraft.perks.gui;

import com.bubblecraft.perks.BubblePerks;
import com.bubblecraft.perks.models.Perk;
import com.bubblecraft.perks.models.PerkType;
import com.bubblecraft.perks.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enhanced GUI for displaying and purchasing perks
 * Features improved visual design, better organization, and enhanced user experience
 */
public class PerksGUI {
    
    private final BubblePerks plugin;
    private static final int GUI_SIZE = 54; // 6 rows
    
    // Border decoration slots
    private static final int[] BORDER_SLOTS = {0, 1, 2, 3, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52, 53};
    private static final int[] SEPARATOR_SLOTS = {9, 18, 27, 36};
    
    public PerksGUI(BubblePerks plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Open the enhanced perks GUI for a player
     */
    public void openGUI(Player player) {
        Component title = ColorUtils.colorizeComponent(
            "<gold><b>Bubble<aqua>Craft <yellow>Perks"
        );
        
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, title);
        
        // Add decorative elements
        addBorders(gui);
        addSeparators(gui);
        
        // Add player info header
        addPlayerInfo(gui, player);
        
        // Add category sections
        addCommandPerks(gui, player);
        addHomePerks(gui, player);
        addVaultPerks(gui, player);
        addJobPerks(gui, player);
        
        // Add navigation/info elements
        addNavigationElements(gui, player);
        
        // Fill remaining empty slots
        fillEmptySlots(gui);
        
        player.openInventory(gui);
    }
    
    /**
     * Add decorative borders to the GUI
     */
    private void addBorders(Inventory gui) {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = border.getItemMeta();
        meta.displayName(Component.empty());
        border.setItemMeta(meta);
        
        for (int slot : BORDER_SLOTS) {
            gui.setItem(slot, border);
        }
    }
    
    /**
     * Add separator lines between sections
     */
    private void addSeparators(Inventory gui) {
        ItemStack separator = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        ItemMeta meta = separator.getItemMeta();
        meta.displayName(ColorUtils.colorizeComponent("<dark_aqua>━━━━━━━━━━━━"));
        separator.setItemMeta(meta);
        
        for (int slot : SEPARATOR_SLOTS) {
            gui.setItem(slot, separator);
        }
    }
    
    /**
     * Add navigation and informational elements
     */
    private void addNavigationElements(Inventory gui, Player player) {
        // Close button
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        closeMeta.displayName(ColorUtils.colorizeComponent("<red><b>✖ Close"));
        List<Component> closeLore = new ArrayList<>();
        closeLore.add(Component.empty());
        closeLore.add(ColorUtils.colorizeComponent("<gray>Click to close this menu"));
        closeMeta.lore(closeLore);
        closeButton.setItemMeta(closeMeta);
        gui.setItem(49, closeButton);
    }
    
    /**
     * Add enhanced player information header
     */
    private void addPlayerInfo(Inventory gui, Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        
        // Use simple gold color instead of gradient
        meta.displayName(ColorUtils.colorizeComponent("<gold><b>" + player.getName() + "'s Perks"));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        
        double balance = plugin.getEconomyManager().getBalance(player);
        lore.add(ColorUtils.colorizeComponent("<white>━━━━━━━━━━━━━━━━━━━━━━"));
        lore.add(ColorUtils.colorizeComponent("<aqua>◈ <white>Balance: <aqua>" + 
            plugin.getEconomyManager().format(balance) + " 🫧"));
        
        int ownedPerks = plugin.getPerkManager().getPlayerPerks(player).size();
        int totalPerks = plugin.getPerkManager().getAllPerks().size();
        lore.add(ColorUtils.colorizeComponent("<aqua>◈ <white>Perks: <green>" + ownedPerks + "<gray>/<aqua>" + totalPerks));
        
        lore.add(ColorUtils.colorizeComponent("<white>━━━━━━━━━━━━━━━━━━━━━━"));
        lore.add(Component.empty());
        lore.add(ColorUtils.colorizeComponent("<gray><i>Click perks to purchase"));
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        gui.setItem(4, item);
    }
    
    /**
     * Add command utility perks with enhanced visual feedback
     */
    private void addCommandPerks(Inventory gui, Player player) {
        // Category header
        ItemStack header = createCategoryHeader(
            Material.COMMAND_BLOCK,
            "<gold>⚙ Utility Commands",
            Arrays.asList(
                "<gray>Quick access to useful tools",
                "<gray>━━━━━━━━━━━━━━━━━━━━"
            )
        );
        gui.setItem(10, header);
        
        // HARDCODED ORDER - Must match InventoryListener slot mapping!
        String[] perkIds = {"grindstone", "workbench", "anvil", "enderchest", "cartographytable", "stonecutter"};
        int[] utilitySlots = {11, 12, 13, 14, 15, 16};
        
        for (int i = 0; i < perkIds.length; i++) {
            Perk perk = plugin.getPerkManager().getPerk(perkIds[i]);
            if (perk != null) {
                ItemStack item = createEnhancedPerkItem(perk, player);
                gui.setItem(utilitySlots[i], item);
            }
        }
    }
    
    /**
     * Add home upgrade perks with enhanced display
     */
    private void addHomePerks(Inventory gui, Player player) {
        // Category header
        ItemStack header = createCategoryHeader(
            Material.RED_BED,
            "<yellow>⌂ Home Upgrades",
            Arrays.asList(
                "<gray>Expand your home limit",
                "<gray>━━━━━━━━━━━━━━━━━━━━"
            )
        );
        gui.setItem(19, header);
        
        // Current homes status
        int currentHomes = getCurrentHomeCount(player);
        ItemStack statusItem = new ItemStack(Material.OAK_SIGN);
        ItemMeta statusMeta = statusItem.getItemMeta();
        statusMeta.displayName(ColorUtils.colorizeComponent("<white>Current Status"));
        List<Component> statusLore = new ArrayList<>();
        statusLore.add(Component.empty());
        statusLore.add(ColorUtils.colorizeComponent("<yellow>◈ <white>Available Homes: <green>" + currentHomes));
        statusLore.add(ColorUtils.colorizeComponent("<yellow>◈ <white>Maximum: <yellow>20"));
        statusLore.add(Component.empty());
        statusMeta.lore(statusLore);
        statusItem.setItemMeta(statusMeta);
        gui.setItem(20, statusItem);
        
        // Add home tier upgrades
        addHomeTierUpgrades(gui, player);
    }
    
    /**
     * Get current home count for player
     */
    private int getCurrentHomeCount(Player player) {
        if (player.hasPermission("essentials.sethome.multiple.tier3")) return 20;
        if (player.hasPermission("essentials.sethome.multiple.tier2")) return 13;
        if (player.hasPermission("essentials.sethome.multiple.tier1")) return 8;
        return 5; // Default
    }
    
    /**
     * Add home tier upgrade items with enhanced visuals
     */
    private void addHomeTierUpgrades(Inventory gui, Player player) {
        String[] tiers = {"tier1", "tier2", "tier3"};
        int[] costs = {100, 150, 200};
        String[] amounts = {"+3", "+5", "+7"};
        int[] totals = {8, 13, 20};
        String[] permissions = {
            "essentials.sethome.multiple.tier1", 
            "essentials.sethome.multiple.tier2", 
            "essentials.sethome.multiple.tier3"
        };
        int[] slots = {21, 22, 23};
        
        for (int i = 0; i < tiers.length; i++) {
            ItemStack item = createProgressiveUpgradeItem(
                player, 
                amounts[i] + " Homes", 
                totals[i] + " Total",
                costs[i], 
                permissions[i],
                i > 0 ? permissions[i-1] : null
            );
            gui.setItem(slots[i], item);
        }
    }
    
    /**
     * Create a category header item
     */
    private ItemStack createCategoryHeader(Material material, String title, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
    // Use unified color handling (supports legacy & MiniMessage)
    meta.displayName(ColorUtils.colorizeComponent("<b>" + title));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        for (String line : description) {
            lore.add(ColorUtils.colorizeComponent(line));
        }
        lore.add(Component.empty());
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Create an enhanced perk item with better visual feedback
     */
    private ItemStack createEnhancedPerkItem(Perk perk, Player player) {
        Material material;
        try {
            material = Material.valueOf(perk.getMaterial().toUpperCase());
        } catch (IllegalArgumentException e) {
            material = Material.STONE;
        }
        
        boolean hasPerk = plugin.getPerkManager().hasPlayerPurchased(player, perk.getId());
        double balance = plugin.getEconomyManager().getBalance(player);
        boolean canAfford = balance >= perk.getCost();
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        // Set display name with status indicator
    String namePrefix = hasPerk ? "&a✔ " : (canAfford ? "&e● " : "&c● ");
    meta.displayName(ColorUtils.colorizeComponent(namePrefix + perk.getName()));
        
        // Set lore
        List<Component> lore = new ArrayList<>();
        for (String line : perk.getLore()) {
            lore.add(ColorUtils.colorizeComponent(line));
        }
        
        lore.add(Component.empty());
    lore.add(ColorUtils.colorizeComponent("&f━━━━━━━━━━━━━━━━━━━━"));
        
        // Add purchase status with enhanced visuals
        if (hasPerk) {
            lore.add(ColorUtils.colorizeComponent("&a&l✔ PURCHASED"));
            lore.add(ColorUtils.colorizeComponent("&7This perk is active!"));
        } else if (canAfford) {
            lore.add(ColorUtils.colorizeComponent("&e● &fCost: &b" + (int)perk.getCost() + " 🫧"));
            lore.add(Component.empty());
            lore.add(ColorUtils.colorizeComponent("&a▸ Click to purchase!"));
        } else {
            lore.add(ColorUtils.colorizeComponent("&c● &fCost: &b" + (int)perk.getCost() + " 🫧"));
            lore.add(ColorUtils.colorizeComponent("&7Your balance: &c" + (int)balance + " 🫧"));
            lore.add(Component.empty());
            lore.add(ColorUtils.colorizeComponent("&c✘ Insufficient funds"));
        }
        
        meta.lore(lore);
        
        // Add enchantment glow effect for purchased items
        if (hasPerk) {
            meta.setEnchantmentGlintOverride(true);
        }
        
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Create a progressive upgrade item (for homes, vaults, jobs)
     */
    private ItemStack createProgressiveUpgradeItem(Player player, String name, String subtitle, 
                                                   int cost, String permission, String requiredPermission) {
        boolean hasPerk = player.hasPermission(permission);
        boolean hasRequirement = requiredPermission == null || player.hasPermission(requiredPermission);
        double balance = plugin.getEconomyManager().getBalance(player);
        boolean canAfford = balance >= cost;
        
        // Determine material and visual state
        Material material;
        String statusColor;
        if (hasPerk) {
            material = Material.LIME_STAINED_GLASS_PANE;
            statusColor = "<green>";
        } else if (!hasRequirement) {
            material = Material.RED_STAINED_GLASS_PANE;
            statusColor = "<red>";
        } else if (canAfford) {
            material = Material.YELLOW_STAINED_GLASS_PANE;
            statusColor = "<yellow>";
        } else {
            material = Material.ORANGE_STAINED_GLASS_PANE;
            statusColor = "<red>";
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        // Display name with status
        String icon = hasPerk ? "✔" : (!hasRequirement ? "🔒" : (canAfford ? "●" : "●"));
        meta.displayName(ColorUtils.colorizeComponent(statusColor + "<b>" + icon + " " + name));
        
        List<Component> lore = new ArrayList<>();
        lore.add(ColorUtils.colorizeComponent("<gray>" + subtitle));
        lore.add(Component.empty());
        
        if (hasPerk) {
            lore.add(ColorUtils.colorizeComponent("<green><b>✔ UNLOCKED"));
            lore.add(ColorUtils.colorizeComponent("<gray>This upgrade is active!"));
        } else if (!hasRequirement) {
            lore.add(ColorUtils.colorizeComponent("<red>🔒 <b>LOCKED"));
            lore.add(ColorUtils.colorizeComponent("<gray>Purchase previous tier first"));
        } else if (canAfford) {
            lore.add(ColorUtils.colorizeComponent("<white>Cost: <aqua>" + cost + " 🫧"));
            lore.add(Component.empty());
            lore.add(ColorUtils.colorizeComponent("<green>▸ Click to purchase!"));
        } else {
            lore.add(ColorUtils.colorizeComponent("<white>Cost: <aqua>" + cost + " 🫧"));
            lore.add(ColorUtils.colorizeComponent("<gray>Your balance: <red>" + (int)balance + " 🫧"));
            lore.add(Component.empty());
            lore.add(ColorUtils.colorizeComponent("<red>✘ Insufficient funds"));
        }
        
        meta.lore(lore);
        
        // Add glow for unlocked items
        if (hasPerk) {
            meta.setEnchantmentGlintOverride(true);
        }
        
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Add vault upgrade perks with enhanced display
     */
    private void addVaultPerks(Inventory gui, Player player) {
        // Category header
        ItemStack header = createCategoryHeader(
            Material.ENDER_CHEST,
            "<light_purple>⚑ Vault Upgrades",
            Arrays.asList(
                "<gray>Unlock additional storage",
                "<gray>━━━━━━━━━━━━━━━━━━━━"
            )
        );
        gui.setItem(28, header);
        
        // Current vault status
        int currentVaults = getCurrentVaultCount(player);
        ItemStack statusItem = new ItemStack(Material.CHEST);
        ItemMeta statusMeta = statusItem.getItemMeta();
        statusMeta.displayName(ColorUtils.colorizeComponent("<white>Current Status"));
        List<Component> statusLore = new ArrayList<>();
        statusLore.add(Component.empty());
        statusLore.add(ColorUtils.colorizeComponent("<light_purple>◈ <white>Available Vaults: <green>" + currentVaults));
        statusLore.add(ColorUtils.colorizeComponent("<light_purple>◈ <white>Maximum: <yellow>5"));
        statusLore.add(Component.empty());
        statusMeta.lore(statusLore);
        statusItem.setItemMeta(statusMeta);
        gui.setItem(29, statusItem);

        // Tier upgrades
        String[] amounts = {"+1 Vault", "+1 Vault", "+2 Vaults"};
        int[] costs = {100, 150, 225};
        int[] totals = {2, 3, 5};
        String[] permissions = {"axvaults.vault.2", "axvaults.vault.3", "axvaults.vault.5"};
        int[] slots = {30, 31, 32};
        
        for (int i = 0; i < amounts.length; i++) {
            ItemStack item = createProgressiveUpgradeItem(
                player,
                amounts[i],
                totals[i] + " Total",
                costs[i],
                permissions[i],
                i > 0 ? permissions[i-1] : null
            );
            gui.setItem(slots[i], item);
        }
    }
    
    /**
     * Get current vault count for player
     */
    private int getCurrentVaultCount(Player player) {
        if (player.hasPermission("axvaults.vault.5")) return 5;
        if (player.hasPermission("axvaults.vault.3")) return 3;
        if (player.hasPermission("axvaults.vault.2")) return 2;
        return 1; // Default
    }

    /**
     * Add job upgrade perks with enhanced display
     */
    private void addJobPerks(Inventory gui, Player player) {
        // Category header
        ItemStack header = createCategoryHeader(
            Material.DIAMOND_PICKAXE,
            "<gold>⚒ Job Upgrades",
            Arrays.asList(
                "<gray>Increase your job capacity",
                "<gray>━━━━━━━━━━━━━━━━━━━━"
            )
        );
        gui.setItem(37, header);
        
        // Current jobs status
        int currentJobs = getCurrentJobCount(player);
        ItemStack statusItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta statusMeta = statusItem.getItemMeta();
        statusMeta.displayName(ColorUtils.colorizeComponent("<white>Current Status"));
        List<Component> statusLore = new ArrayList<>();
        statusLore.add(Component.empty());
        statusLore.add(ColorUtils.colorizeComponent("<gold>◈ <white>Available Jobs: <green>" + currentJobs));
        statusLore.add(ColorUtils.colorizeComponent("<gold>◈ <white>Maximum: <yellow>6"));
        statusLore.add(Component.empty());
        statusMeta.lore(statusLore);
        statusItem.setItemMeta(statusMeta);
        gui.setItem(38, statusItem);

        // Tier upgrades
        String[] amounts = {"+1 Job", "+1 Job", "+1 Job"};
        int[] costs = {100, 150, 200};
        int[] totals = {4, 5, 6};
        String[] permissions = {"jobs.max.4", "jobs.max.5", "jobs.max.6"};
        int[] slots = {39, 40, 41};
        
        for (int i = 0; i < amounts.length; i++) {
            ItemStack item = createProgressiveUpgradeItem(
                player,
                amounts[i],
                totals[i] + " Total",
                costs[i],
                permissions[i],
                i > 0 ? permissions[i-1] : null
            );
            gui.setItem(slots[i], item);
        }
    }
    
    /**
     * Get current job count for player
     */
    private int getCurrentJobCount(Player player) {
        if (player.hasPermission("jobs.max.6")) return 6;
        if (player.hasPermission("jobs.max.5")) return 5;
        if (player.hasPermission("jobs.max.4")) return 4;
        return 3; // Default
    }
    
    
    /**
     * Fill empty slots with subtle glass panes
     */
    private void fillEmptySlots(Inventory gui) {
        ItemStack filler = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        meta.displayName(Component.empty());
        filler.setItemMeta(meta);
        
        for (int i = 0; i < GUI_SIZE; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, filler);
            }
        }
    }
}
