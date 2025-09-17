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
import java.util.List;

/**
 * GUI for displaying and purchasing perks
 */
public class PerksGUI {
    
    private final BubblePerks plugin;
    private static final int GUI_SIZE = 54; // 6 rows
    
    public PerksGUI(BubblePerks plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Open the perks GUI for a player
     */
    public void openGUI(Player player) {
        Component title = ColorUtils.colorizeComponent(
            "<gold><b>Bubble<aqua>Craft <#FFC380>ᴘ<#E9AF87>ᴇ<#D39C8F>ʀ<#BC8896>ᴋ<#A6749D>ꜱ"
        );
        
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, title);
        
        // Add player info
        addPlayerInfo(gui, player);
        
        // Add perks
        addCommandPerks(gui, player);
        addHomePerks(gui, player);
        addVaultPerks(gui, player);
        addJobPerks(gui, player);
        
        // Fill empty slots with glass panes
        fillEmptySlots(gui);
        
        player.openInventory(gui);
    }
    
    /**
     * Add player information item
     */
    private void addPlayerInfo(Inventory gui, Player player) {
        ItemStack item = new ItemStack(Material.WIND_CHARGE);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(ColorUtils.colorizeComponent("<white>" + player.getName()));
        
        List<Component> lore = new ArrayList<>();
        lore.add(ColorUtils.colorizeComponent("<dark_gray>ʙᴀʟᴀɴᴄᴇ: " + 
            plugin.getEconomyManager().format(plugin.getEconomyManager().getBalance(player))));
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        // Get player info slot from config
        int playerInfoSlot = plugin.getConfigManager().getConfig().getInt("gui.slots.player-info", 4);
        gui.setItem(playerInfoSlot, item);
    }
    
    /**
     * Add command perks (grindstone, craft, anvil, echest, cartographytable, stonecutter)
     */
    private void addCommandPerks(Inventory gui, Player player) {
        List<Perk> commandPerks = plugin.getPerkManager().getPerksByType(PerkType.COMMAND);
        
        // Map each perk to its configured slot
        for (Perk perk : commandPerks) {
            String slotPath = "gui.slots.utilities." + perk.getId();
            int slot = plugin.getConfigManager().getConfig().getInt(slotPath, -1);
            
            if (slot >= 0 && slot < GUI_SIZE) {
                ItemStack item = createPerkItem(perk, player);
                gui.setItem(slot, item);
            }
        }
    }
    
    /**
     * Add home upgrade perks
     */
    private void addHomePerks(Inventory gui, Player player) {
        // Main homes info
        ItemStack homeInfo = new ItemStack(Material.RED_BED);
        ItemMeta meta = homeInfo.getItemMeta();
        
        meta.displayName(ColorUtils.colorizeComponent("<#bf9f81><b>⌂ /home"));
        
        List<Component> lore = new ArrayList<>();
        lore.add(ColorUtils.colorizeComponent("<dark_gray>ᴜᴘɢʀᴀᴅᴇ"));
        lore.add(ColorUtils.colorizeComponent("<#bf9f81>▪ <white>Increase your maximum amount!"));
        lore.add(ColorUtils.colorizeComponent("<#bf9f81>▪ <white>Up to 20 total homes."));
        lore.add(Component.empty());
        lore.add(ColorUtils.colorizeComponent("<white>◆ You have <#bf9f81><u>5<white> homes available."));
        
        meta.lore(lore);
        homeInfo.setItemMeta(meta);
        
        // Get home info slot from config
        int homeInfoSlot = plugin.getConfigManager().getConfig().getInt("gui.slots.homes.info", 12);
        gui.setItem(homeInfoSlot, homeInfo);
        
        // Add home upgrade items
        addHomeTierUpgrades(gui, player);
    }
    
    /**
     * Add home tier upgrade items
     */
    private void addHomeTierUpgrades(Inventory gui, Player player) {
        String[] tiers = {"tier1", "tier2", "tier3"};
        int[] costs = {100, 150, 200};
        String[] amounts = {"+3", "+5", "+7"};
        String[] permissions = {"essentials.sethome.multiple.tier1", "essentials.sethome.multiple.tier2", "essentials.sethome.multiple.tier3"};
        
        for (int i = 0; i < tiers.length; i++) {
            int slot = plugin.getConfigManager().getConfig().getInt("gui.slots.homes." + tiers[i], -1);
            if (slot >= 0 && slot < GUI_SIZE) {
                ItemStack item = createTierUpgradeItem(player, amounts[i] + " Homes", costs[i], permissions[i]);
                gui.setItem(slot, item);
            }
        }
    }
    
    /**
     * Create a tier upgrade item
     */
    private ItemStack createTierUpgradeItem(Player player, String name, int cost, String permission) {
        Material material = player.hasPermission(permission) ? Material.GREEN_STAINED_GLASS_PANE : 
                           (plugin.getEconomyManager().getBalance(player) >= cost ? Material.YELLOW_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(ColorUtils.colorizeComponent("<green><b>" + name));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        
        if (player.hasPermission(permission)) {
            lore.add(ColorUtils.colorizeComponent("<light_purple>Purchased!"));
        } else {
            double balance = plugin.getEconomyManager().getBalance(player);
            if (balance >= cost) {
                lore.add(ColorUtils.colorizeComponent("<white>◆ Cost: <green>" + cost + "🫧 <green>✔"));
            } else {
                lore.add(ColorUtils.colorizeComponent("<white>◆ Cost: <red>" + (int)balance + "<gray>/<#5ccfe6>" + cost + "🫧 <red>❌"));
            }
        }
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Add vault upgrade perks
     */
    private void addVaultPerks(Inventory gui, Player player) {
        // Info item
        int infoSlot = plugin.getConfigManager().getConfig().getInt("gui.slots.vaults.info", -1);
        if (infoSlot >= 0 && infoSlot < GUI_SIZE) {
            ItemStack vaultInfo = createInfoItem("Vault Perks", "<white>Store your items with safety!");
            gui.setItem(infoSlot, vaultInfo);
        }

        // Tier upgrades
        String[] tiers = {"tier1", "tier2", "tier3"};
        int[] costs = {100, 200, 300};
        String[] amounts = {"2", "3", "4"};
        String[] permissions = {"bubbleperks.vault.tier1", "bubbleperks.vault.tier2", "bubbleperks.vault.tier3"};
        
        for (int i = 0; i < tiers.length; i++) {
            int slot = plugin.getConfigManager().getConfig().getInt("gui.slots.vaults." + tiers[i], -1);
            if (slot >= 0 && slot < GUI_SIZE) {
                ItemStack item = createTierUpgradeItem(player, amounts[i] + " Vaults", costs[i], permissions[i]);
                gui.setItem(slot, item);
            }
        }
    }

    /**
     * Add job upgrade perks
     */
    private void addJobPerks(Inventory gui, Player player) {
        // Info item
        int infoSlot = plugin.getConfigManager().getConfig().getInt("gui.slots.jobs.info", -1);
        if (infoSlot >= 0 && infoSlot < GUI_SIZE) {
            ItemStack jobInfo = createInfoItem("Job Perks", "<white>Increase your job capacity!");
            gui.setItem(infoSlot, jobInfo);
        }

        // Tier upgrades
        String[] tiers = {"tier1", "tier2", "tier3"};
        int[] costs = {150, 250, 350};
        String[] amounts = {"4", "5", "6"};
        String[] permissions = {"bubbleperks.jobs.tier1", "bubbleperks.jobs.tier2", "bubbleperks.jobs.tier3"};
        
        for (int i = 0; i < tiers.length; i++) {
            int slot = plugin.getConfigManager().getConfig().getInt("gui.slots.jobs." + tiers[i], -1);
            if (slot >= 0 && slot < GUI_SIZE) {
                ItemStack item = createTierUpgradeItem(player, amounts[i] + " Jobs", costs[i], permissions[i]);
                gui.setItem(slot, item);
            }
        }
    }
    
    /**
     * Create an info item for category headers
     */
    private ItemStack createInfoItem(String title, String description) {
        ItemStack item = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(ColorUtils.colorizeComponent("<aqua><b>" + title));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(ColorUtils.colorizeComponent(description));
        lore.add(Component.empty());
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Create an item stack for a perk
     */
    private ItemStack createPerkItem(Perk perk, Player player) {
        Material material;
        try {
            material = Material.valueOf(perk.getMaterial().toUpperCase());
        } catch (IllegalArgumentException e) {
            material = Material.STONE;
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        // Set display name
        meta.displayName(ColorUtils.colorizeComponent(perk.getName()));
        
        // Set lore
        List<Component> lore = new ArrayList<>();
        for (String line : perk.getLore()) {
            lore.add(ColorUtils.colorizeComponent(line));
        }
        
        // Add purchase status
        boolean hasPerk = plugin.getPerkManager().hasPlayerPurchased(player, perk.getId());
        double balance = plugin.getEconomyManager().getBalance(player);
        
        if (hasPerk) {
            lore.add(Component.empty());
            lore.add(ColorUtils.colorizeComponent("<light_purple>Purchased!"));
        } else if (balance >= perk.getCost()) {
            lore.add(Component.empty());
            lore.add(ColorUtils.colorizeComponent(
                "<white>◆ Cost: <green>" + (int)perk.getCost() + "🫧 <green>✔"
            ));
        } else {
            lore.add(Component.empty());
            lore.add(ColorUtils.colorizeComponent(
                "<white>◆ Cost: <red>" + (int)balance + "<gray>/<#5ccfe6>" + (int)perk.getCost() + "🫧 <red>❌"
            ));
        }
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Fill empty slots with glass panes
     */
    private void fillEmptySlots(Inventory gui) {
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
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
